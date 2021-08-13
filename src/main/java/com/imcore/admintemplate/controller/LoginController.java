package com.imcore.admintemplate.controller;

import com.imcore.admintemplate.base.context.login.LoginContextHolder;
import com.imcore.admintemplate.base.model.PagePermission;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysUser;
import com.imcore.admintemplate.model.vo.RouteVo;
import com.imcore.admintemplate.service.ISysUserService;
import com.imcore.admintemplate.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 获取登录用户基本信息，角色信息及权限信息
     */
    @GetMapping("/getUserInfo")
    public BaseResponse getUserInfo() throws ImcoreBusinessException{
        SysUser user = sysUserService.getById(LoginContextHolder.me().getLoginUserId());
        user.setPassword(null);
        user.setUsername(null);
        user.setPhone(null);
        user.setSalt(null);
        Map<String,Object> map = new HashMap<>();
        map.put("user",user);

        //获取用户角色集合
        map.put("roles", LoginContextHolder.me().getLoginUserRolesWithOutPrefix());

        //查询用户页面准入权限及页面按钮权限
        List<PagePermission> pagePermissionList = loginService.selectUserPagePermission();
        map.put("permissions",pagePermissionList);
        return BaseResponse.success(map);
    }

    /**
     * 获取登录用户拥有的菜单
     * @return
     */
    @GetMapping("/routes")
    public BaseResponse routes(){
        //查询用户拥有的菜单(目录和页面)
        List<RouteVo> routeVos = loginService.selectUserRoutes();
        return BaseResponse.success(routeVos);
    }

}
