package com.imcore.admintemplate.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.base.context.login.LoginContextHolder;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.base.model.PagePermission;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.mapper.SysUserRoleMapper;
import com.imcore.admintemplate.model.bo.SysMenu;
import com.imcore.admintemplate.model.bo.SysUser;
import com.imcore.admintemplate.mapper.SysUserMapper;
import com.imcore.admintemplate.model.dto.SysUserPasswordDto;
import com.imcore.admintemplate.model.vo.RouteVo;
import com.imcore.admintemplate.model.vo.SysUserVo;
import com.imcore.admintemplate.service.ISysMenuService;
import com.imcore.admintemplate.service.ISysRoleMenuService;
import com.imcore.admintemplate.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imcore.admintemplate.utils.EncryptUtil;
import com.imcore.admintemplate.utils.MD5Util;
import com.imcore.admintemplate.utils.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-08
 */
@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysUser selectByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public Page<SysUserVo> userListAll(BasePage basePage) {
        Page<SysUserVo> page = new Page<>(basePage.getCurrent(),basePage.getSize());
        //查询所有的用户,过滤掉superAdmin
        List<SysUserVo> sysUserVos = sysUserMapper.selectList(page);
        for(SysUserVo userVo:sysUserVos){
            List<String> roleNames = sysUserRoleMapper.selectRoleNamesByUserId(userVo.getId());
            userVo.setRoles(StringUtils.join(roleNames.toArray(), ","));
        }
        page.setRecords(sysUserVos);
        return page;
    }

    @Override
    public void userAdd(SysUser sysUser) throws ImcoreBusinessException {
        if(StringUtils.isBlank(sysUser.getPassword())){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }
        if(sysUser.getUsername().equals(CommonConstant.DEFAULT_USER_ADMIN_USERNAME)){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_SUPER_ADMIN_NOT_ALLOW_OPERATE);
        }

        SysUser dbUser1 = sysUserMapper.selectByUsername(sysUser.getUsername());
        if(dbUser1 != null){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_USERNAME_EXIST);
        }
        SysUser dbUser2 = sysUserMapper.selectByPhone(sysUser.getPhone());
        if(dbUser2 != null){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_PHONE_EXIST);
        }
        sysUser.setId(UuidUtil.getUUID());
        sysUser.setStatus(CommonConstant.USER_STATUS_ENABLE);
        sysUser.setCreatedOn(new Date());
        sysUser.setUpdatedOn(new Date());
        sysUser.setAvatar("https://gw.alipayobjects.com/zos/rmsportal/WhxKECPNujWoWEFNdnJE.png");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String finalPassword = encoder.encode(sysUser.getPassword());
        String phone = sysUser.getPhone();
        /**
         * 手机号加密存储到数据库,aes加密方式,利用了用户密码作为加密的密钥,可在代码中调用EncryptUtil.aes_decrypt解密,
         * 也可以在sql语句中里面利用mysql自带函数AES_DECRYPT解密: select AES_DECRYPT(UNHEX(phone), password) as phone from sys_user
         * 如果需要手机号模糊查询时,sql写法: select * from sys_user where AES_DECRYPT(UNHEX(phone), password) like concat('%',#{phone},'%')
         */
        String finalPhone = EncryptUtil.aes_encrypt(phone,finalPassword);
        sysUser.setPhone(finalPhone);
        sysUser.setPassword(finalPassword);
        sysUserMapper.insert(sysUser);
    }

    @Override
    public void userEdit(SysUser sysUser) throws ImcoreBusinessException {
        if(StringUtils.isBlank(sysUser.getId())){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }
        if(sysUser.getUsername().equals(CommonConstant.DEFAULT_USER_ADMIN_USERNAME)){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_SUPER_ADMIN_NOT_ALLOW_OPERATE);
        }
        if(StringUtils.isNotBlank(sysUser.getPassword())){
            //密码不允许在编辑界面修改,如果传了这个值直接设置为null,就不会更新密码了, 密码只允许重置
            sysUser.setPassword(null);
        }
        SysUser dbUser1 = sysUserMapper.selectByUsername(sysUser.getUsername());
        if(dbUser1 != null && !dbUser1.getId().equals(sysUser.getId())){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_USERNAME_EXIST);
        }

        SysUser dbUser2 = sysUserMapper.selectByPhone(sysUser.getPhone());
        if(dbUser2 != null && !dbUser2.getId().equals(sysUser.getId())){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_PHONE_EXIST);
        }

        SysUser user = sysUserMapper.selectById(sysUser.getId());
        if(user == null){
            throw new ImcoreBusinessException(ResultCodeEnum.DB_DATA_NO_EXISTS);
        }
        user.setUsername(sysUser.getUsername());
        user.setName(sysUser.getName());
        user.setSex(sysUser.getSex());
        user.setEmail(sysUser.getEmail());
        user.setUpdatedOn(new Date());
        user.setPhone(EncryptUtil.aes_encrypt(sysUser.getPhone(),user.getPassword()));
        sysUserMapper.updateById(user);
    }

    @Override
    public void deleteById(String id) throws ImcoreBusinessException {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser == null){
            throw new ImcoreBusinessException(ResultCodeEnum.DB_DATA_NO_EXISTS);
        }
        if(sysUser.getUsername().equals(CommonConstant.DEFAULT_USER_ADMIN_USERNAME)){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_SUPER_ADMIN_NOT_ALLOW_OPERATE);
        }
        sysUserMapper.deleteById(id);
        sysUserRoleMapper.deleteByUserId(id);
    }

    @Override
    public void userRestPassword(String id) throws ImcoreBusinessException{
        if(StringUtils.isBlank(id)){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }
        SysUser dbUser = sysUserMapper.selectById(id);
        if(dbUser == null){
            throw new ImcoreBusinessException(ResultCodeEnum.DB_DATA_NO_EXISTS);
        }
        if(dbUser.getUsername().equals(CommonConstant.DEFAULT_USER_ADMIN_USERNAME)){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_SUPER_ADMIN_NOT_ALLOW_OPERATE);
        }
        String rawPhone = EncryptUtil.aes_decrypt(dbUser.getPhone(),dbUser.getPassword());
        String md5Password = MD5Util.MD5(CommonConstant.DEFAULT_PASSWORD);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String finalPassword = encoder.encode(md5Password);
        dbUser.setPassword(finalPassword);
        dbUser.setPhone(EncryptUtil.aes_encrypt(rawPhone,finalPassword));
        sysUserMapper.updateById(dbUser);
    }

    @Override
    public void editUserStatus(SysUser sysUser) throws ImcoreBusinessException{
        if(StringUtils.isBlank(sysUser.getId()) || sysUser.getStatus() == null){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }
        SysUser dbUser = sysUserMapper.selectById(sysUser.getId());
        if(dbUser == null){
            throw new ImcoreBusinessException(ResultCodeEnum.DB_DATA_NO_EXISTS);
        }
        if(dbUser.getUsername().equals(CommonConstant.DEFAULT_USER_ADMIN_USERNAME)){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_SUPER_ADMIN_NOT_ALLOW_OPERATE);
        }
        dbUser.setStatus(sysUser.getStatus());
        sysUserMapper.updateById(dbUser);
    }

    @Override
    public void userEditPassword(SysUserPasswordDto passwordDto) throws ImcoreBusinessException{
        SysUser sysUser = sysUserMapper.selectById(LoginContextHolder.me().getLoginUserId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(encoder.matches(passwordDto.getOldPassword(),sysUser.getPassword())){
            String rawPhone = EncryptUtil.aes_decrypt(sysUser.getPhone(),sysUser.getPassword());
            String finalPassword = encoder.encode(passwordDto.getNewPassword());
            sysUser.setPassword(finalPassword);
            sysUser.setPhone(EncryptUtil.aes_encrypt(rawPhone,finalPassword));
            sysUserMapper.updateById(sysUser);
        }else{
            throw new ImcoreBusinessException(ResultCodeEnum.USER_OLD_PASSWORD_ERROR);
        }
    }

    @Override
    public void updateSaltByUsername(String username, String salt) {
        sysUserMapper.updateSaltByUsername(username,salt);
    }



}
