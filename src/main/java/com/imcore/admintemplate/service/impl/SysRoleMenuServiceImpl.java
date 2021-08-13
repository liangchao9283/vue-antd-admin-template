package com.imcore.admintemplate.service.impl;

import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysMenu;
import com.imcore.admintemplate.model.bo.SysRoleMenu;
import com.imcore.admintemplate.mapper.SysRoleMenuMapper;
import com.imcore.admintemplate.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imcore.admintemplate.utils.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-20
 */
@Service
@Transactional
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<String> roleOwnMenu(String roleId) {
        return sysRoleMenuMapper.roleOwnMenu(roleId);
    }

    @Override
    public void roleGrantMenu(String roleId, List<String> grantMenuIdList)throws ImcoreBusinessException {
        if(StringUtils.isBlank(roleId)){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }
        sysRoleMenuMapper.deleteByRoleId(roleId);
        for(String menuId: grantMenuIdList){
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setId(UuidUtil.getUUID());
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setCreatedOn(new Date());
            sysRoleMenu.setUpdatedOn(new Date());
            sysRoleMenuMapper.insert(sysRoleMenu);
        }
    }

    @Override
    public List<String> selectInterfacePermissionsByRoleIds(List<String> roleIds) {
        if(roleIds == null || roleIds.size() == 0){
            return null;
        }
        return sysRoleMenuMapper.selectInterfacePermissionsByRoleIds(roleIds);
    }


    @Override
    public List<SysMenu> selectOnlyMenuByRoleIds(List<String> roleIds) {
        if(roleIds == null ||roleIds.size() ==0){
            return null;
        }
        return sysRoleMenuMapper.selectOnlyMenuByRoleIds(roleIds);
    }

    @Override
    public List<SysMenu> selectPageMenuByRoleIds(List<String> roleIds) {
        if(roleIds == null ||roleIds.size() ==0){
            return null;
        }
        return sysRoleMenuMapper.selectPageMenuByRoleIds(roleIds);
    }

    @Override
    public List<SysMenu> selectPermissionsByRoleIdsAndMenuId(List<String> roleIds, String menuId) {
        if(roleIds == null ||roleIds.size() ==0){
            return null;
        }
        return sysRoleMenuMapper.selectPermissionsByRoleIdsAndMenuId(roleIds,menuId);
    }

}
