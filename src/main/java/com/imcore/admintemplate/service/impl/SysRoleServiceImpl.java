package com.imcore.admintemplate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.mapper.SysRoleMenuMapper;
import com.imcore.admintemplate.mapper.SysUserRoleMapper;
import com.imcore.admintemplate.model.bo.SysRole;
import com.imcore.admintemplate.mapper.SysRoleMapper;
import com.imcore.admintemplate.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imcore.admintemplate.utils.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-19
 */
@Service
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public Page<SysRole> roleListAll(BasePage basePage) {
        Page<SysRole> page = new Page<>(basePage.getCurrent(),basePage.getSize());
        //查询所有角色,过滤掉super_admin角色
        List<SysRole> list = sysRoleMapper.selectList(page);
        page.setRecords(list);
        return page;
    }

    @Override
    public void roleAdd(SysRole sysRole) throws ImcoreBusinessException{
        validatorParams(sysRole);

        SysRole dbRole = sysRoleMapper.selectByCode(sysRole.getCode());
        if(dbRole != null){
            throw new ImcoreBusinessException(ResultCodeEnum.ROLE_CODE_REPETITIVE);
        }
        sysRole.setId(UuidUtil.getUUID());
        sysRole.setCreatedOn(new Date());
        sysRole.setUpdatedOn(new Date());
        sysRoleMapper.insert(sysRole);
    }

    @Override
    public void roleEdit(SysRole sysRole) throws ImcoreBusinessException{
        if(StringUtils.isBlank(sysRole.getId())){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_MISS);
        }
        validatorParams(sysRole);

        SysRole existRole = sysRoleMapper.selectByCode(sysRole.getCode());
        if(existRole != null && !existRole.getId().equals(sysRole.getId())){
            throw new ImcoreBusinessException(ResultCodeEnum.ROLE_CODE_REPETITIVE);
        }
        SysRole dbRole = sysRoleMapper.selectById(sysRole.getId());
        if(dbRole == null){
            throw new ImcoreBusinessException(ResultCodeEnum.DB_DATA_NO_EXISTS);
        }
        dbRole.setRoleName(sysRole.getRoleName());
        dbRole.setRemark(sysRole.getRemark());
        dbRole.setUpdatedOn(new Date());
        sysRoleMapper.updateById(dbRole);
    }

    private static void validatorParams(SysRole sysRole) throws ImcoreBusinessException{
        if(sysRole.getRoleName().equals(CommonConstant.DEFAULT_ROLE_ADMIN_NAME)){
            throw new ImcoreBusinessException(ResultCodeEnum.ROLE_SUPER_ADMIN_NOT_ALLOW_OPERATE);
        }

        if(sysRole.getCode().equals(CommonConstant.DEFAULT_ROLE_ADMIN_CODE)){
            throw new ImcoreBusinessException(ResultCodeEnum.ROLE_SUPER_ADMIN_CODE_EXIST);
        }
        if(sysRole.getCode().toUpperCase().startsWith("ROLE_")){
            throw new ImcoreBusinessException(ResultCodeEnum.ROLE_CODE_PREFIX_ERROR);
        }

        boolean matches = Pattern.matches("^[^\\u4e00-\\u9fa5]+$", sysRole.getCode());
        if(!matches){
            throw new ImcoreBusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
    }

    @Override
    public void roleDeleteById(String id) throws ImcoreBusinessException{
        SysRole sysRole = sysRoleMapper.selectById(id);
        if(sysRole == null){
            throw new ImcoreBusinessException(ResultCodeEnum.DB_DATA_NO_EXISTS);
        }
        if(sysRole.getCode().equals(CommonConstant.DEFAULT_ROLE_ADMIN_CODE)){
            throw new ImcoreBusinessException(ResultCodeEnum.ROLE_SUPER_ADMIN_NOT_ALLOW_OPERATE);
        }
        sysRoleMapper.deleteById(id);
        sysRoleMenuMapper.deleteByRoleId(id);
        sysUserRoleMapper.deleteByRoleId(id);
    }

    @Override
    public List<SysRole> findAll() {
        return sysRoleMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public SysRole selectByCode(String code) {
        return sysRoleMapper.selectByCode(code);
    }
}
