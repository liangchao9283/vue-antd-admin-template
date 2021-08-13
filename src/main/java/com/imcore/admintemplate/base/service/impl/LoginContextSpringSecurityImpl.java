
package com.imcore.admintemplate.base.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.base.consts.SymbolConstant;
import com.imcore.admintemplate.base.context.login.LoginContext;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.base.model.OAuthUser;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.utils.SecurityUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * 登录用户上下文实现类
 *
 */
@Component
public class LoginContextSpringSecurityImpl implements LoginContext {

    private LoginContextSpringSecurityImpl() {

    }

    /**
     * 获取当前登录用户
     *
     */
    @Override
    public OAuthUser getLoginUser() {
        Authentication authentication = SecurityUtil.getAuthentication();
        if (ObjectUtil.isEmpty(authentication) || authentication.getPrincipal() instanceof String) {
            throw new ImcoreBusinessException(ResultCodeEnum.USER_NO_EXIST);
        } else {
            return (OAuthUser) authentication.getPrincipal();
        }
    }

    /**
     * 获取当前登录用户，如未登录，则返回null，不抛异常
     *
     */
    @Override
    public OAuthUser getLoginUserWithoutException() {
        Authentication authentication = SecurityUtil.getAuthentication();
        if (ObjectUtil.isEmpty(authentication) || authentication.getPrincipal() instanceof String) {
            return null;
        } else {
            return (OAuthUser) authentication.getPrincipal();
        }
    }

    /**
     * 获取当前登录用户的id
     *
     */
    @Override
    public String getLoginUserId() {
        return this.getLoginUser().getId();
    }

    /**
     * 判断用户是否登录
     *
     */
    @Override
    public boolean hasLogin() {
        Authentication authentication = SecurityUtil.getAuthentication();
        if (ObjectUtil.isEmpty(authentication) || authentication.getPrincipal() instanceof String) {
            return false;
        } else {
            return !(authentication instanceof AnonymousAuthenticationToken);
        }
    }

    /**
     * 获取当前登录的用户账号
     *
     */
    @Override
    public String getLoginUsername() {
        return this.getLoginUser().getUsername();
    }



    /**
     * 判断当前登录用户是否包含某个角色
     *
     */
    @Override
    public boolean hasRole(String roleCode) {
        List<String> roleCodeList = this.getLoginUserRolesWithOutPrefix();
        return roleCodeList.contains(roleCode);
    }

    /**
     * 判断当前登录用户是否包含任意一个角色
     *
     */
    @Override
    public boolean hasAnyRole(String roleCodes) {
        boolean flag = false;
        List<String> loginUserRoleCodeList = this.getLoginUserRolesWithOutPrefix();
        String[] roleCodeArr = StrUtil.split(roleCodes, SymbolConstant.COMMA);
        for (String roleCode : roleCodeArr) {
            if (loginUserRoleCodeList.contains(roleCode)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断当前登录用户是否是超级管理员
     *
     */
    @Override
    public boolean isSuperAdmin() {
        return SecurityUtil.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_"+CommonConstant.DEFAULT_ROLE_ADMIN_CODE));
    }

    /**
     * 判断当前登录用户是否包含所有角色
     *
     */
    @Override
    public boolean hasAllRole(String roleCodes) {
        boolean flag = true;
        List<String> loginUserRoleCodeList = this.getLoginUserRolesWithOutPrefix();
        String[] roleCodeArr = StrUtil.split(roleCodes, SymbolConstant.COMMA);
        for (String roleCode : roleCodeArr) {
            if (!loginUserRoleCodeList.contains(roleCode)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 获取当前用户的角色编码集合
     *
     */
    public List<String> getLoginUserRolesWithPrefix() {
        return this.getLoginUser().getRoles();
    }

    @Override
    public List<String> getLoginUserRolesWithOutPrefix() {
        return this.getLoginUser().getRolesWithOutPrefix();
    }
}
