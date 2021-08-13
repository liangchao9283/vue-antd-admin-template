package com.imcore.admintemplate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.model.bo.SysVisLog;
import com.imcore.admintemplate.model.dto.SysVisLogDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统访问日志mapper
 */
public interface SysVisLogMapper extends BaseMapper<SysVisLog> {
    List<SysVisLog> selectList(@Param("dto") SysVisLogDto dto,@Param("page") Page<SysVisLog> page);

}
