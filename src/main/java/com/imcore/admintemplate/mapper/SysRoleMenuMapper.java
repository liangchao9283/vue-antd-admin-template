package com.imcore.admintemplate.mapper;

import com.imcore.admintemplate.model.bo.SysMenu;
import com.imcore.admintemplate.model.bo.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liang chao
 * @since 2021-07-20
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 查询指定角色拥有的菜单数据(包含按钮权限数据)
     * @param roleId   角色id
     * @return  菜单数据(包含按钮权限数据)
     */
    List<String> roleOwnMenu(String roleId);

    void deleteByRoleId(String roleId);

    void deleteByMenuId(String menuId);

    /**
     * 根据角色id集合查询拥有的所有接口权限(接口权限包含页面准入权限及按钮权限,即sys_menu表中type=1和2的permission_code集合)
     * @param roleIds 角色id集合
     * @return permission_code集合
     */
    List<String> selectInterfacePermissionsByRoleIds(@Param("roleIds") List<String> roleIds);

    /**
     * 根据角色id集合查询拥有的所有菜单数据(菜单数据不包括按钮权限数据,即只查询sys_menu表中type=0和1的,不查询type=2的)
     * @param roleIds 角色id集合
     * @return 菜单数据集合
     */
    List<SysMenu> selectOnlyMenuByRoleIds(@Param("roleIds") List<String> roleIds);

    /**
     * 根据角色id集合查询拥有的页面菜单数据(即只查询sys_menu表中type=1的数据)
     * @param roleIds 角色id集合
     * @return 菜单数据集合
     */
    List<SysMenu> selectPageMenuByRoleIds(@Param("roleIds") List<String> roleIds);

    /**
     * 查询用户在某个页面下拥有的按钮权限数据(即查询sys_menu表中type=2的数据)
     * @param roleIds   角色id集合
     * @param menuId  某个具体页面的菜单id
     * @return  按钮权限集合
     */
    List<SysMenu> selectPermissionsByRoleIdsAndMenuId(@Param("roleIds") List<String> roleIds, String menuId);
}
