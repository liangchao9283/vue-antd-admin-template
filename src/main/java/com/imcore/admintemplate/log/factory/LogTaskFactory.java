package com.imcore.admintemplate.log.factory;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.Log;
import com.imcore.admintemplate.base.annotion.BusinessLog;
import com.imcore.admintemplate.model.bo.SysOpLog;
import com.imcore.admintemplate.model.bo.SysVisLog;
import com.imcore.admintemplate.service.SysOpLogService;
import com.imcore.admintemplate.service.SysVisLogService;
import org.aspectj.lang.JoinPoint;


import java.util.TimerTask;


/**
 * 日志操作任务创建工厂
 *
 */
public class LogTaskFactory {

    private static final Log log = Log.get();

    private static final SysVisLogService sysVisLogService = SpringUtil.getBean(SysVisLogService.class);

    private static final SysOpLogService sysOpLogService = SpringUtil.getBean(SysOpLogService.class);

    /**
     * 登录日志
     *
     */
    public static TimerTask loginLog(SysVisLog sysVisLog, final String username, String success, String failMessage) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysLoginLog(sysVisLog, username, success, failMessage);
                    sysVisLogService.save(sysVisLog);
                } catch (Exception e) {
                    log.error(">>> 创建登录日志异常，登录账号为：{}，具体信息为：{}", username, e.getMessage());
                }
            }
        };
    }

    /**
     * 登出日志
     *
     */
    public static TimerTask exitLog(SysVisLog sysVisLog, String username) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysExitLog(sysVisLog, username);
                    sysVisLogService.save(sysVisLog);
                } catch (Exception e) {
                    log.error(">>> 创建退出日志异常，登录账号为：{}，具体信息为：{}", username, e.getMessage());
                }
            }
        };
    }

    /**
     * 操作日志
     *
     */
    public static TimerTask operationLog(SysOpLog sysOpLog, String username, BusinessLog businessLog, JoinPoint joinPoint, String result) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysOperationLog(sysOpLog, username, businessLog, joinPoint, result);
                    sysOpLogService.save(sysOpLog);
                } catch (Exception e) {
                    log.error(">>> 创建操作日志异常，操作账户为：{}，具体信息为：{}", username, e.getMessage());
                }
            }
        };
    }

    /**
     * 异常日志
     *
     */
    public static TimerTask exceptionLog(SysOpLog sysOpLog, String username, BusinessLog businessLog, JoinPoint joinPoint, Exception exception) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    LogFactory.createSysExceptionLog(sysOpLog, username, businessLog, joinPoint, exception);
                    sysOpLogService.save(sysOpLog);
                } catch (Exception e) {
                    log.error(">>> 创建异常日志异常，操作账户为：{}，具体信息为：{}", username, e.getMessage());
                }
            }
        };
    }
}
