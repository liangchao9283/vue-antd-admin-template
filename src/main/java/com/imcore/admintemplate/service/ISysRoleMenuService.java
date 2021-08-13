package com.imcore.admintemplate.service;

import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysMenu;
import com.imcore.admintemplate.model.bo.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-20
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 查询指定角色拥有的菜单数据(包含按钮权限数据)
     * @param roleId   角色id
     * @return  菜单数据(包含按钮权限数据)
     */
    List<String> roleOwnMenu(String roleId);

    /**
     * 为角色授权菜单(包括按钮权限)
     * @param roleId    需要授权的角色id
     * @param grantMenuIdList   授予角色的菜单id集合(包括按钮权限id)
     * @throws ImcoreBusinessException
     */
    void roleGrantMenu(String roleId,List<String> grantMenuIdList) throws ImcoreBusinessException;

    /**
     * 根据角色id集合查询拥有的所有接口权限(接口权限包含页面准入权限及按钮权限,即sys_menu表中type=1和2的permission_code集合)
     * @param roleIds 角色id集合
     * @return permission_code集合
     */
    List<String> selectInterfacePermissionsByRoleIds(List<String> roleIds);

    /**
     * 根据角色id集合查询拥有的所有菜单数据(菜单数据不包括按钮权限数据,即只查询sys_menu表中type=0和1的,不查询type=2的)
     * @param roleIds 角色id集合
     * @return 菜单数据集合
     */
    List<SysMenu> selectOnlyMenuByRoleIds(List<String> roleIds);

    /**
     * 根据角色id集合查询拥有的页面菜单数据(即只查询sys_menu表中type=1的数据)
     * @param roleIds 角色id集合
     * @return 菜单数据集合
     */
    List<SysMenu> selectPageMenuByRoleIds(List<String> roleIds);

    /**
     * 查询用户在某个页面下拥有的按钮权限数据
     * @param roleIds   角色id集合
     * @param menuId  某个具体页面的菜单id
     * @return  按钮权限集合
     */
    List<SysMenu> selectPermissionsByRoleIdsAndMenuId(List<String> roleIds,String menuId);

}
