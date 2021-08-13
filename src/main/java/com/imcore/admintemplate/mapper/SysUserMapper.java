package com.imcore.admintemplate.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.model.bo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imcore.admintemplate.model.vo.SysUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liang chao
 * @since 2021-07-08
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser selectByUsername(String username);

    SysUser selectByPhone(String phone);

    /**
     * 查询所有的用户,过滤掉superAdmin用户
     * @param page
     * @return
     */
    List<SysUserVo> selectList(@Param("page") Page<SysUserVo> page);

    void updateSaltByUsername(@Param("username") String username,@Param("salt") String salt);
}
