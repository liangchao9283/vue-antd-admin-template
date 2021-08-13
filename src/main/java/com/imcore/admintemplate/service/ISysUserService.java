package com.imcore.admintemplate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.base.model.PagePermission;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imcore.admintemplate.model.dto.SysUserPasswordDto;
import com.imcore.admintemplate.model.vo.RouteVo;
import com.imcore.admintemplate.model.vo.SysUserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-08
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser selectByUsername(String username);

    Page<SysUserVo> userListAll(BasePage basePage);

    void userAdd(SysUser sysUser) throws ImcoreBusinessException;

    void userEdit(SysUser sysUser) throws ImcoreBusinessException;

    void deleteById(String id) throws ImcoreBusinessException;

    void userRestPassword(String id) throws ImcoreBusinessException;

    void  editUserStatus(SysUser sysUser) throws ImcoreBusinessException;

    void userEditPassword(SysUserPasswordDto passwordDto) throws ImcoreBusinessException;

    void updateSaltByUsername(String id,String salt);
}
