package com.imcore.admintemplate.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.mapper.SysRoleMenuMapper;
import com.imcore.admintemplate.model.bo.MenuPermission;
import com.imcore.admintemplate.model.bo.SysMenu;
import com.imcore.admintemplate.mapper.SysMenuMapper;
import com.imcore.admintemplate.model.bo.SysRole;
import com.imcore.admintemplate.model.dto.SysMenuDto;
import com.imcore.admintemplate.model.vo.MenuTreeVo;
import com.imcore.admintemplate.model.vo.SysMenuVo;
import com.imcore.admintemplate.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imcore.admintemplate.service.ISysRoleService;
import com.imcore.admintemplate.utils.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-10
 */
@Service
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public Page<SysMenuVo> menuListAll(BasePage basePage) {
        Page<SysMenuVo> page = new Page<>(basePage.getCurrent(),basePage.getSize());
        List<SysMenuVo> list = sysMenuMapper.menuListByParentIdWithPage(CommonConstant.MENU_TOP_LEVEL_ID, page);
        for (SysMenuVo vo : list) {
            vo.setChildren(buildMenuChildren(vo.getId()));
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public List<MenuTreeVo> getMenuTree() {
        return buildMenuTree(CommonConstant.MENU_TOP_LEVEL_ID);
    }

    @Override
    public List<MenuTreeVo> treeForGrant(String roleId) {
        return buildTreeForGrant(CommonConstant.MENU_TOP_LEVEL_ID);
    }

    @Override
    public void menuAdd(SysMenuDto dto) throws ImcoreBusinessException {
        SysMenu menu = new SysMenu();
        if(dto.getComponent().equals("system/sysMenu")){
            //"菜单管理"菜单不允许添加进数据库,这个菜单不能用来分配给角色,如果用户拥有"super_admin"角色,则会默认返回"菜单管理"的路由信息
            //主要是怕普通用户获得"菜单管理"菜单后瞎搞,乱删菜单数据或乱改数据,导致页面不能访问,只能超级管理员可以操作"菜单管理"菜单
            throw new ImcoreBusinessException(ResultCodeEnum.MENU_COMPONENT_NAME_SYSTEM_SYSMENU_CANNOT_ADD);
        }
        if(dto.getComponent().startsWith("/") || dto.getComponent().startsWith("pages")){
            throw new ImcoreBusinessException(ResultCodeEnum.MENU_COMPONENT_NAME_RULE_ERROR);
        }
        if(dto.getPath().startsWith("/")){
            throw new ImcoreBusinessException(ResultCodeEnum.MENU_PATH_RULE_ERROR);
        }

        BeanUtils.copyProperties(dto,menu);
        String menuId = UuidUtil.getUUID();
        menu.setId(menuId);
        menu.setCreatedOn(new Date());
        menu.setUpdatedOn(new Date());
        if(menu.getType().equals(CommonConstant.MENU_TYPE_PAGE)){
            menu.setPermissionCode(menu.getPath());

            //type = 1表示菜单为页面,如果页面有按钮权限,需要添加按钮权限
            List<MenuPermission> permissionsData = dto.getPermissionsData();
            if(permissionsData != null && permissionsData.size() > 0){
                for(MenuPermission menuPermission: permissionsData){
                    if(sysMenuMapper.selectPermissionCode(menuPermission.getPermissionCode()) != null){
                        throw new ImcoreBusinessException(ResultCodeEnum.buildErrorMsg("权限值:"+menuPermission.getPermissionCode()+"已存在,不能重复添加!"));
                    }
                    sysMenuMapper.insert(setPermission(menuPermission,menuId));
                }
            }
        }else if(menu.getType().equals(CommonConstant.MENU_TYPE_DIRECTORY)){
            //目录
            menu.setPermissionCode("*");
        }
        sysMenuMapper.insert(menu);
    }

    @Override
    public void menuEdit(SysMenuDto dto) throws ImcoreBusinessException {
        if(StringUtils.isBlank(dto.getId())){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }

        if(dto.getComponent().equals("system/sysMenu")){
            //"菜单管理"菜单不允许添加进数据库,这个菜单不能用来分配给角色,如果用户拥有"super_admin"角色,则会默认返回"菜单管理"的路由信息
            throw new ImcoreBusinessException(ResultCodeEnum.MENU_COMPONENT_NAME_SYSTEM_SYSMENU_CANNOT_ADD);
        }
        if(dto.getComponent().startsWith("/") || dto.getComponent().startsWith("pages")){
            throw new ImcoreBusinessException(ResultCodeEnum.MENU_COMPONENT_NAME_RULE_ERROR);
        }
        if(dto.getPath().startsWith("/")){
            throw new ImcoreBusinessException(ResultCodeEnum.MENU_PATH_RULE_ERROR);
        }
        SysMenu dbMenu = sysMenuMapper.selectById(dto.getId());
        if(dbMenu == null){
            throw new ImcoreBusinessException(ResultCodeEnum.DB_DATA_NO_EXISTS);
        }
        BeanUtils.copyProperties(dto,dbMenu);
        dbMenu.setUpdatedOn(new Date());
        if(dto.getType().equals(CommonConstant.MENU_TYPE_DIRECTORY)){
            dbMenu.setPermissionCode("*");
        }else if(dto.getType().equals(CommonConstant.MENU_TYPE_PAGE)){
            dbMenu.setPermissionCode(dbMenu.getPath());
        }
        sysMenuMapper.updateById(dbMenu);

        if(dto.getType().equals(CommonConstant.MENU_TYPE_PAGE)){
            List<MenuPermission> permissionsData = dto.getPermissionsData();

            List<MenuPermission> dbPermissions = sysMenuMapper.permissionListByParentId(dto.getId());
            for(MenuPermission dbPermission: dbPermissions){
                boolean flag = false;
                if(permissionsData != null && permissionsData.size() > 0){
                    for(MenuPermission menuPermission: permissionsData){
                        if(dbPermission.getId().equals(menuPermission.getId())){
                            flag = true;
                            break;
                        }
                    }
                }
                if(!flag){
                    sysMenuMapper.deleteById(dbPermission.getId());
                }
            }
            if(permissionsData != null && permissionsData.size() > 0){
                for(MenuPermission menuPermission: permissionsData){
                    if(StringUtils.isNotBlank(menuPermission.getId())){
                        SysMenu permission = sysMenuMapper.selectById(menuPermission.getId());
                        permission.setName(menuPermission.getName());
                        permission.setPermissionCode(menuPermission.getPermissionCode());
                        permission.setUpdatedOn(new Date());
                        sysMenuMapper.updateById(permission);
                    }else{
                        sysMenuMapper.insert(setPermission(menuPermission,dto.getId()));
                    }
                }
            }
        }
    }

    private SysMenu setPermission(MenuPermission menuPermission,String menuId){
        SysMenu permission = new SysMenu();
        permission.setId(UuidUtil.getUUID());
        permission.setName(menuPermission.getName());
        permission.setParentId(menuId);
        permission.setType(CommonConstant.MENU_TYPE_BUTTON);
        permission.setPermissionCode(menuPermission.getPermissionCode());
        permission.setCreatedOn(new Date());
        permission.setUpdatedOn(new Date());
        return permission;
    }

    @Override
    public SysMenuVo getById(String id) {
        SysMenuVo sysMenuVo = new SysMenuVo();
        SysMenu menu = sysMenuMapper.selectById(id);
        BeanUtils.copyProperties(menu,sysMenuVo);
        if(menu.getType().equals(CommonConstant.MENU_TYPE_PAGE)){
            //查询菜单下的按钮权限
            List<MenuPermission> permissionData = sysMenuMapper.permissionListByParentId(id);
            if(permissionData != null && permissionData.size() >0){
                sysMenuVo.setPermissionsData(permissionData);
            }
        }
        return sysMenuVo;
    }

    @Override
    public void deleteById(String id) {
        sysMenuMapper.deleteById(id);
        sysRoleMenuMapper.deleteByMenuId(id);
        //如果删除是目录,同时删除目录下所有页面及按钮权限
        List<String> children = sysMenuMapper.selectIdByParentId(id);
        if(children !=null && children.size() >0){
            for(String childrenId: children){
                deleteById(childrenId);
            }
        }
    }

    @Override
    public List<SysMenu> selectSystemWeightMenus() {
        return sysMenuMapper.selectSystemWeightMenus();
    }

    private List<SysMenuVo> buildMenuChildren(String parentId) {
        List<SysMenuVo> children = sysMenuMapper.menuListByParentId(parentId,null);
        if (children != null && children.size() > 0) {
            for (SysMenuVo vo : children) {
                vo.setChildren(buildMenuChildren(vo.getId()));
            }
        }
        return children;
    }


    private List<MenuTreeVo> buildMenuTree(String parentId){
        List<Integer> types = new ArrayList<>();
        types.add(CommonConstant.MENU_TYPE_DIRECTORY);
        types.add(CommonConstant.MENU_TYPE_PAGE);
        List<SysMenuVo> children = sysMenuMapper.menuListByParentId(parentId,types);
        List<MenuTreeVo> menuTreeVoList = new ArrayList<>();
        if (children != null && children.size() > 0) {
            for (SysMenuVo vo : children) {
                MenuTreeVo menuTreeVo = new MenuTreeVo();
                menuTreeVo.setId(vo.getId());
                menuTreeVo.setParentId(vo.getParentId());
                menuTreeVo.setTitle(vo.getName());
                menuTreeVo.setValue(vo.getId());
                menuTreeVo.setChildren(buildMenuTree(menuTreeVo.getId()));
                menuTreeVoList.add(menuTreeVo);
            }
        }
        return menuTreeVoList;
    }


    private List<MenuTreeVo> buildTreeForGrant(String parentId){
        List<Integer> types = new ArrayList<>();
        types.add(CommonConstant.MENU_TYPE_DIRECTORY);
        types.add(CommonConstant.MENU_TYPE_PAGE);
        types.add(CommonConstant.MENU_TYPE_BUTTON);
        List<SysMenuVo> children = sysMenuMapper.menuListByParentId(parentId,types);
        List<MenuTreeVo> menuTreeVoList = new ArrayList<>();
        if (children != null && children.size() > 0) {
            for (SysMenuVo vo : children) {
                MenuTreeVo menuTreeVo = new MenuTreeVo();
                menuTreeVo.setId(vo.getId());
                menuTreeVo.setParentId(vo.getParentId());
                menuTreeVo.setTitle(vo.getName());
                menuTreeVo.setValue(vo.getId());
                menuTreeVo.setChildren(buildTreeForGrant(menuTreeVo.getId()));
                menuTreeVoList.add(menuTreeVo);
            }
        }
        return menuTreeVoList;
    }
}
