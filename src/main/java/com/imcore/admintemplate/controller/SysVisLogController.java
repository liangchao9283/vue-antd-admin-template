package com.imcore.admintemplate.controller;

import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.model.dto.SysVisLogDto;
import com.imcore.admintemplate.service.SysVisLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问日志控制器
 */
@RestController
@RequestMapping("/sysVisLog")
public class SysVisLogController {

    @Autowired
    private SysVisLogService sysVisLogService;

    @PostMapping("/")
    public BaseResponse selectList(@RequestBody SysVisLogDto dto){
        return BaseResponse.success(sysVisLogService.selectList(dto));
    }
}
