package com.imcore.admintemplate.log;

import com.imcore.admintemplate.base.annotion.BusinessLog;
import com.imcore.admintemplate.model.bo.SysOpLog;
import com.imcore.admintemplate.model.bo.SysVisLog;
import com.imcore.admintemplate.log.factory.LogFactory;
import com.imcore.admintemplate.log.factory.LogTaskFactory;
import com.imcore.admintemplate.utils.HttpServletUtil;
import com.imcore.admintemplate.utils.IpAddressUtil;
import com.imcore.admintemplate.utils.UaUtil;
import org.aspectj.lang.JoinPoint;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;


import javax.servlet.http.HttpServletRequest;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 日志管理器
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:13
 */
public class LogManager {

    /**
     * 异步操作记录日志的线程池
     */
    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(10, new ScheduledExecutorFactoryBean());

    private LogManager() {
    }

    private static final LogManager LOG_MANAGER = new LogManager();

    public static LogManager me() {
        return LOG_MANAGER;
    }

    /**
     * 异步执行日志的方法
     *
     * @author xuyuxiang
     * @date 2020/4/8 19:19
     */
    private void executeLog(TimerTask task) {

        //如果演示模式开启，则不记录日志
       /* if (ConstantContextHolder.getDemoEnvFlag()) {
            return;
        }*/

        //日志记录操作延时
        int operateDelayTime = 10;
        EXECUTOR.schedule(task, operateDelayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 登录日志
     *
     * @author xuyuxiang
     * @date 2020/3/18 20:00
     */
    public void executeLoginLog(final String username, final String success, final String failMessage) {
        SysVisLog sysVisLog = this.genBaseSysVisLog();
        TimerTask timerTask = LogTaskFactory.loginLog(sysVisLog, username,
                success,
                failMessage);
        executeLog(timerTask);
    }

    /**
     * 登出日志
     *
     * @author xuyuxiang
     * @date 2020/3/18 20:01
     */
    public void executeExitLog(final String username) {
        SysVisLog sysVisLog = this.genBaseSysVisLog();
        TimerTask timerTask = LogTaskFactory.exitLog(sysVisLog, username);
        executeLog(timerTask);
    }

    /**
     * 操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/18 20:01
     */
    public void executeOperationLog(BusinessLog businessLog, final String username, JoinPoint joinPoint, final String result) {
        SysOpLog sysOpLog = this.genBaseSysOpLog();
        TimerTask timerTask = LogTaskFactory.operationLog(sysOpLog, username, businessLog, joinPoint, result);
        executeLog(timerTask);
    }

    /**
     * 异常日志
     *
     * @author xuyuxiang
     * @date 2020/3/18 20:01
     */
    public void executeExceptionLog(BusinessLog businessLog, final String username, JoinPoint joinPoint, Exception exception) {
        SysOpLog sysOpLog = this.genBaseSysOpLog();
        TimerTask timerTask = LogTaskFactory.exceptionLog(sysOpLog, username, businessLog, joinPoint, exception);
        executeLog(timerTask);
    }

    /**
     * 构建基础访问日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:44
     */
    private SysVisLog genBaseSysVisLog() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        String ip = IpAddressUtil.getIp(request);
        String address = IpAddressUtil.getAddress(request);

        String browser = UaUtil.getBrowser(request);
        String os = UaUtil.getOs(request);
        return LogFactory.genBaseSysVisLog(ip, address, browser, os);

    }

    /**
     * 构建基础操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:44
     */
    private SysOpLog genBaseSysOpLog() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        String ip = IpAddressUtil.getIp(request);
        String address = IpAddressUtil.getAddress(request);
        String browser = UaUtil.getBrowser(request);
        String os = UaUtil.getOs(request);
        String url = request.getRequestURI();
        String method = request.getMethod();
        return LogFactory.genBaseSysOpLog(ip, address, browser, os, url, method);
    }

}
