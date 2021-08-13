package com.imcore.admintemplate.controller;

import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.model.dto.SysOpLogDto;
import com.imcore.admintemplate.service.SysOpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志控制器
 */
@RestController
@RequestMapping("/sysOpLog")
public class SysOpLogController {

    @Autowired
    private SysOpLogService sysOpLogService;

    @PostMapping("/")
    public BaseResponse selectList(@RequestBody SysOpLogDto dto){
        return BaseResponse.success(sysOpLogService.selectList(dto));
    }
}


