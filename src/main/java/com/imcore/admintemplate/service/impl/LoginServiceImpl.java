package com.imcore.admintemplate.service.impl;

import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.base.context.login.LoginContextHolder;
import com.imcore.admintemplate.base.model.PagePermission;
import com.imcore.admintemplate.model.bo.SysMenu;
import com.imcore.admintemplate.model.vo.RouteVo;
import com.imcore.admintemplate.service.ISysMenuService;
import com.imcore.admintemplate.service.ISysRoleMenuService;
import com.imcore.admintemplate.service.ISysUserRoleService;
import com.imcore.admintemplate.service.LoginService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;
    @Autowired
    private ISysMenuService sysMenuService;

    @Override
    public List<RouteVo> selectUserRoutes() {
        //如果是超级管理员则展示所有系统权重菜单，不能展示业务权重菜单
        if(LoginContextHolder.me().isSuperAdmin()){
            List<SysMenu> sysMenus = sysMenuService.selectSystemWeightMenus();
            return buildRoute(CommonConstant.MENU_TOP_LEVEL_ID, sysMenus);
        }else{
            //如果不是超级管理员,则查询被分配的菜单
            //查询用户拥有的所有角色id
            List<String> roleIds = sysUserRoleService.selectRoleIdsByUserId(LoginContextHolder.me().getLoginUserId());
            //查询用户拥有的所的目录和页面菜单(type=0 or type =1)
            List<SysMenu> sysMenus = sysRoleMenuService.selectOnlyMenuByRoleIds(roleIds);
            return buildRoute(CommonConstant.MENU_TOP_LEVEL_ID, sysMenus);
        }

    }

    @Override
    public List<PagePermission> selectUserPagePermission() {
        List<PagePermission> pagePermissionList = new ArrayList<>();
        //查询用户拥有的所有角色id
        List<String> roleIds = sysUserRoleService.selectRoleIdsByUserId(LoginContextHolder.me().getLoginUserId());
        //查询用户拥有的所有页面菜单(只查type=1的)
        List<SysMenu> pageMenus = sysRoleMenuService.selectPageMenuByRoleIds(roleIds);
        if(pageMenus != null && pageMenus.size() >0){
            for(SysMenu pageMenu: pageMenus){
                //查询在当前页面下,用户拥有的按钮权限
                List<SysMenu> pageButtonMenus = sysRoleMenuService.selectPermissionsByRoleIdsAndMenuId(roleIds, pageMenu.getId());
                PagePermission pagePermission = new PagePermission();
                pagePermission.setId(pageMenu.getPermissionCode());
                ArrayList<String> operation = new ArrayList<>();
                if(pageButtonMenus != null && pageButtonMenus.size()>0){
                    for(SysMenu pageButton: pageButtonMenus){
                        operation.add(pageButton.getPermissionCode());
                    }
                }
                pagePermission.setOperation(operation);
                pagePermissionList.add(pagePermission);
            }
        }

        return pagePermissionList;
    }

    private static List<RouteVo> buildRoute(String parentId, List<SysMenu> sysMenus){
        List<RouteVo> routeVos = new ArrayList<>();
        if(sysMenus != null && sysMenus.size() >0){
            for(SysMenu menu: sysMenus){
                if(menu.getParentId().equals(parentId)){
                    RouteVo route = new RouteVo();
                    BeanUtils.copyProperties(menu,route);
                    //authority是前端用来控制页面准入权限的,取值为菜单数据的permissionCode,目录级菜单的permissionCode值都为"*",表示不需要准入权限,页面级菜单的permissionCode默认等于path的值,
                    //前端是这样来控制页面权限的: 通过路由配置决定用户有哪些菜单(当然路由也可以从后台接口获取),即使有了菜单,如果没有准入权限,点击菜单会跳403页面
                    //页面需要什么准入权限是由Authority字段来决定的,用户在登录时返回的permissions数组中给予相应的权限,那么用户就能进入此页面
                    // 返回给前端的permissions中的id对应authority,只有permissions中有id对应authority,才能进入此页面,参见LoginController中getUserInfo方法
                    route.setAuthority(menu.getPermissionCode());
                    route.setRouter(route.getPath());
                    routeVos.add(route);
                }
            }
            if(routeVos.size() > 0){
                for(RouteVo routeVo : routeVos){
                    routeVo.setChildren(buildRoute(routeVo.getId(),sysMenus));
                }
            }
        }
        return routeVos;
    }
}
