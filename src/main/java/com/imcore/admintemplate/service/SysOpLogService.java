package com.imcore.admintemplate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imcore.admintemplate.base.model.BasePage;
import com.imcore.admintemplate.model.bo.SysOpLog;
import com.imcore.admintemplate.model.dto.SysOpLogDto;

/**
 * 系统操作日志service接口
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:21
 */
public interface SysOpLogService extends IService<SysOpLog> {

   Page<SysOpLog> selectList(SysOpLogDto dto);
}
