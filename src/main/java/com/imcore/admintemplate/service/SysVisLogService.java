package com.imcore.admintemplate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.model.bo.SysVisLog;
import com.imcore.admintemplate.model.dto.SysVisLogDto;

public interface SysVisLogService extends IService<SysVisLog> {

    Page<SysVisLog> selectList(SysVisLogDto dto);

}
