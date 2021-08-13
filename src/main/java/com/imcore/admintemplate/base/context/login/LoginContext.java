
package com.imcore.admintemplate.base.context.login;
import com.imcore.admintemplate.base.model.OAuthUser;

import java.util.List;

/**
 * 登录用户上下文
 *
 * @author xuyuxiang
 * @date 2020/3/13 12:16
 */
public interface LoginContext {

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户信息
     */
    OAuthUser getLoginUser();

    /**
     * 获取当前登录用户，如未登录，则返回null，不抛异常
     *
     * @return 当前登录用户信息
     */
    OAuthUser getLoginUserWithoutException();

    /**
     * 获取当前登录用户的id
     *
     * @return 当前登录用户的id
     */
    String getLoginUserId();

    /**
     * 判断用户是否登录
     *
     * @return 是否登录，true是，false否
     */
    boolean hasLogin();

    /**
     * 获取当前登录用户的账户
     *
     * @return 当前登陆用户的账户account
     * @author xuyuxiang
     * @date 2020/3/19 20:37
     */
    String getLoginUsername();


    /**
     * 判断当前登录用户是否包含某个角色
     *
     * @param roleCode 角色编码
     * @return 是否包含该角色，true是，false否
     */
    boolean hasRole(String roleCode);

    /**
     * 判断当前登录用户是否包含任意一个角色
     *
     * @param roleCodes 角色集合，逗号拼接
     * @return 是否包含任一角色，true是，false否
     */
    boolean hasAnyRole(String roleCodes);

    /**
     * 判断当前登录用户是否是超级管理员
     *
     * @return 当前登录用户是否是超级管理员
     */
    boolean isSuperAdmin();

    /**
     * 判断当前登录用户是否包含所有角色
     *
     * @param roleCodes 角色集合，逗号拼接
     * @return 是否包含所有角色，true是，false否
     */
    boolean hasAllRole(String roleCodes);

    /**
     * 获取当前登录用户的所有角色code,角色code带前缀“ROLE_"
     *
     * @return 角色code集合
     */
    List<String> getLoginUserRolesWithPrefix();

    /**
     * 获取当前登录用户的所有角色,,角色code不带前缀“ROLE_"
     *
     * @return 角色code集合
     */
    List<String> getLoginUserRolesWithOutPrefix();

}
