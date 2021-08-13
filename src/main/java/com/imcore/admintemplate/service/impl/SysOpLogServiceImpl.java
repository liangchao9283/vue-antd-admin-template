package com.imcore.admintemplate.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.model.bo.SysOpLog;
import com.imcore.admintemplate.mapper.SysOpLogMapper;
import com.imcore.admintemplate.model.dto.SysOpLogDto;
import com.imcore.admintemplate.service.SysOpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysOpLogServiceImpl extends ServiceImpl<SysOpLogMapper, SysOpLog> implements SysOpLogService {
    @Autowired
    private SysOpLogMapper sysOpLogMapper;


    @Override
    public Page<SysOpLog> selectList(SysOpLogDto dto) {
        Page<SysOpLog> page = new Page<>(dto.getCurrent(),dto.getSize());
        List<SysOpLog> list = sysOpLogMapper.selectList(dto,page);
        page.setRecords(list);
        return page;
    }
}
