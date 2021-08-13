package com.imcore.admintemplate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-19
 */
public interface ISysRoleService extends IService<SysRole> {

    Page<SysRole> roleListAll(BasePage basePage);

    void roleAdd(SysRole sysRole) throws ImcoreBusinessException;

    void roleEdit(SysRole sysRole) throws ImcoreBusinessException;

    void roleDeleteById(String id) throws ImcoreBusinessException;

    List<SysRole> findAll();

    SysRole selectByCode(String code);

}
