package com.imcore.admintemplate.service.impl;

import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.mapper.SysUserMapper;
import com.imcore.admintemplate.model.bo.SysUser;
import com.imcore.admintemplate.model.bo.SysUserRole;
import com.imcore.admintemplate.mapper.SysUserRoleMapper;
import com.imcore.admintemplate.service.ISysUserRoleService;
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
 * @since 2021-07-22
 */
@Service
@Transactional
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<String> selectRoleIdsByUserId(String userId) {
        return sysUserRoleMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public void userGrantRole(String userId, List<String> grantRoleIdList) throws ImcoreBusinessException {
        if(StringUtils.isBlank(userId)){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }
        if(grantRoleIdList == null || grantRoleIdList.size() ==0){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }
        SysUser sysUser = sysUserMapper.selectById(userId);
        if(sysUser != null && sysUser.getUsername().equals(CommonConstant.DEFAULT_USER_ADMIN_USERNAME)){
            throw new ImcoreBusinessException(ResultCodeEnum.USER_SUPER_ADMIN_NOT_ALLOW_OPERATE);
        }
        sysUserRoleMapper.deleteByUserId(userId);
        for(String roleId: grantRoleIdList){
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setId(UuidUtil.getUUID());
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCreatedOn(new Date());
            sysUserRole.setUpdatedOn(new Date());
            sysUserRoleMapper.insert(sysUserRole);
        }
    }

    @Override
    public List<String> selectRoleCodesByUserId(String userId) {
        return sysUserRoleMapper.selectRoleCodesByUserId(userId);
    }
}
