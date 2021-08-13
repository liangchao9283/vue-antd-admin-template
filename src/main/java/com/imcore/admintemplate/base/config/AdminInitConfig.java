package com.imcore.admintemplate.base.config;

import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.model.bo.SysRole;
import com.imcore.admintemplate.model.bo.SysUser;
import com.imcore.admintemplate.model.bo.SysUserRole;
import com.imcore.admintemplate.service.ISysRoleService;
import com.imcore.admintemplate.service.ISysUserRoleService;
import com.imcore.admintemplate.service.ISysUserService;
import com.imcore.admintemplate.utils.MD5Util;
import com.imcore.admintemplate.utils.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class AdminInitConfig implements CommandLineRunner {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Override
    public void run(String... args) throws Exception {
        log.info("检查是否存在super_admin角色 ===========");
        SysRole sysRole = sysRoleService.selectByCode(CommonConstant.DEFAULT_ROLE_ADMIN_CODE);
        if(sysRole == null){
            log.info("super_admin角色不存在 **********");
            sysRole = new SysRole();
            sysRole.setId(UuidUtil.getUUID());
            sysRole.setRoleName(CommonConstant.DEFAULT_ROLE_ADMIN_NAME);
            sysRole.setCode(CommonConstant.DEFAULT_ROLE_ADMIN_CODE);
            sysRole.setCreatedOn(new Date());
            sysRole.setUpdatedOn(new Date());
            sysRoleService.save(sysRole);
            log.info("新增super_admin角色成功 **********");
        }
        log.info("检查是否存在superAdmin账户 ===========");
        SysUser sysUser = sysUserService.selectByUsername(CommonConstant.DEFAULT_USER_ADMIN_USERNAME);
        if(sysUser == null){
            log.info("superAdmin账户不存在 ************");
            sysUser = new SysUser();
            sysUser.setId(UuidUtil.getUUID());
            sysUser.setUsername(CommonConstant.DEFAULT_USER_ADMIN_USERNAME);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            sysUser.setPassword(encoder.encode(MD5Util.MD5("superAdmin123456;")));
            sysUser.setSex(1);
            sysUser.setStatus(CommonConstant.USER_STATUS_ENABLE);
            sysUser.setName(CommonConstant.DEFAULT_USER_ADMIN_NAME);
            sysUser.setCreatedOn(new Date());
            sysUser.setUpdatedOn(new Date());
            sysUser.setAvatar("https://gw.alipayobjects.com/zos/rmsportal/WhxKECPNujWoWEFNdnJE.png");
            sysUserService.save(sysUser);

            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setId(UuidUtil.getUUID());
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(sysRole.getId());
            sysUserRole.setCreatedOn(new Date());
            sysUserRole.setUpdatedOn(new Date());
            sysUserRoleService.save(sysUserRole);

            log.info("新增superAdmin用户成功 **********");
        }

       /* log.debug("我是debug日志*****");
        log.info("我是info日志*****");
        log.warn("我是warn日志*****");
        log.error("我是error日志*****");*/

    }

}
