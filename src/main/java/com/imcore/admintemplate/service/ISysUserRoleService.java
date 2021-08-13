package com.imcore.admintemplate.service;

import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.model.bo.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liang chao
 * @since 2021-07-22
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    List<String> selectRoleIdsByUserId(String userId);

    void userGrantRole(String userId, List<String> grantRoleIdList) throws ImcoreBusinessException;

    List<String> selectRoleCodesByUserId(String userId);


}
