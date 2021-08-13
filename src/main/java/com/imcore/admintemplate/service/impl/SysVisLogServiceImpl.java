package com.imcore.admintemplate.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.model.bo.SysVisLog;
import com.imcore.admintemplate.mapper.SysVisLogMapper;
import com.imcore.admintemplate.model.dto.SysVisLogDto;
import com.imcore.admintemplate.service.SysVisLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问日志service接口实现类
 *
 */
@Service
public class SysVisLogServiceImpl extends ServiceImpl<SysVisLogMapper, SysVisLog> implements SysVisLogService {
    @Autowired
    private SysVisLogMapper sysVisLogMapper;

    @Override
    public Page<SysVisLog> selectList(SysVisLogDto dto) {
        Page<SysVisLog> page = new Page<>(dto.getCurrent(),dto.getSize());
        List<SysVisLog> list = sysVisLogMapper.selectList(dto,page);
        page.setRecords(list);
        return page;
    }
}
