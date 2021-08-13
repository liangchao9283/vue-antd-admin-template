package com.imcore.admintemplate.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.model.bo.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author liang chao
 * @since 2021-07-19
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectList(Page<SysRole> page);

    SysRole selectByCode(String code);
}
