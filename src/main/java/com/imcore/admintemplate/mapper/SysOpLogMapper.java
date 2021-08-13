package com.imcore.admintemplate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imcore.admintemplate.model.bo.SysOpLog;
import com.imcore.admintemplate.model.dto.SysOpLogDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统访问日志mapper
 */
public interface SysOpLogMapper extends BaseMapper<SysOpLog> {

    List<SysOpLog> selectList(@Param("dto") SysOpLogDto dto, @Param("page") Page<SysOpLog> page);
}
