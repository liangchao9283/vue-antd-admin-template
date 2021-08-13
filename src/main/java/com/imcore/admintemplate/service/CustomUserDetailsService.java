package com.imcore.admintemplate.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.imcore.admintemplate.base.model.OAuthUser;
import com.imcore.admintemplate.base.model.BaseToken;
import com.imcore.admintemplate.log.LogManager;
import com.imcore.admintemplate.model.bo.SysUser;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Override
    public UserDetails loadUserByUsername(String username){
        SysUser sysUser = sysUserService.selectByUsername(username);

        if(sysUser == null) {
            throw new UsernameNotFoundException("账号不存在");
        }
        boolean disabled = false;
        if(sysUser.getStatus() == 2){
            //throw new DisabledException("账户已被禁用");
            disabled = true;
        }
        //查询用户拥有的所有角色code
        List<String> roleCodes = sysUserRoleService.selectRoleCodesByUserId(sysUser.getId());
        String[] roles = new String[]{};

        if(roleCodes != null && roleCodes.size() >0){
            final int roleCodesSize = roleCodes.size();
            roles = roleCodes.toArray(new String[roleCodesSize]);
        }

        //查询用户拥有的所有角色id
        List<String> roleIds = sysUserRoleService.selectRoleIdsByUserId(sysUser.getId());
        //查询用户拥有的所有接口权限(对应菜单数据中type=2的,实际上页面的准入权限及页面上每一个按钮的权限都对应一个接口的权限)
        List<String> interfacePermissions = sysRoleMenuService.selectInterfacePermissionsByRoleIds(roleIds);
        String[] authorities = new String[]{};
        if(interfacePermissions != null && interfacePermissions.size() > 0 ){
            final int interfacePermissionsSize = interfacePermissions.size();
            authorities = interfacePermissions.toArray(new String[interfacePermissionsSize]);
        }
        return OAuthUser.builder()
                .id(sysUser.getId())
                .username(sysUser.getUsername())
                .password(sysUser.getPassword())
                .salt(sysUser.getSalt())
                .disabled(disabled)
                .roles(roles)
                .authorities(authorities)
                .build();
    }


    public BaseToken saveUserLoginInfo(UserDetails user){
        String salt = BCrypt.gensalt(); //BCrypt.gensalt();  调用该方法实时生成加密的salt
        //将salt保存到数据库或者缓存中
        sysUserService.updateSaltByUsername(user.getUsername(),salt);

        Algorithm algorithm = Algorithm.HMAC256(salt);

        Date expireAt = DateUtils.addHours(new Date(),2);  //设置2小时后过期
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expireAt)
                .withIssuedAt(new Date())
                .withIssuer("imcore.com")
                .sign(algorithm);
        BaseToken baseToken = new BaseToken();
        baseToken.setToken(token);
        baseToken.setExpireAt(expireAt.getTime());
        return baseToken;
    }

    public void deleteUserLoginInfo(String username) {
        /**
         * 退出登录时，清除数据库或者缓存中登录salt
         */
        sysUserService.updateSaltByUsername(username,"");
        LogManager.me().executeExitLog(username);
    }
}
