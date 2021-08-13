package com.imcore.admintemplate.mapper;

import com.imcore.admintemplate.model.bo.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liang chao
 * @since 2021-07-22
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<String> selectRoleIdsByUserId(String userId);

    void deleteByUserId(String userId);

    void deleteByRoleId(String roleId);

    List<String> selectRoleCodesByUserId(String userId);

    List<String> selectRoleNamesByUserId(String userId);
}
