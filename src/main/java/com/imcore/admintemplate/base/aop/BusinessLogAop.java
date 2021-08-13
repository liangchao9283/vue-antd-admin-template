package com.imcore.admintemplate.base.aop;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.imcore.admintemplate.base.annotion.BusinessLog;
import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.base.context.login.LoginContextHolder;
import com.imcore.admintemplate.base.model.OAuthUser;
import com.imcore.admintemplate.log.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 业务日志aop切面
 *
 * @author liangchao
 * @date 2021/8/7
 */
@Aspect
@Order(100)
public class BusinessLogAop {

    /**
     * 日志切入点
     */
    @Pointcut("@annotation(com.imcore.admintemplate.base.annotion.BusinessLog)")
    private void getLogPointCut() {
    }

    /**
     * 操作成功返回结果记录日志
     */
    @AfterReturning(pointcut = "getLogPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);
        String username = getLoginUsername();
        //异步记录日志
        LogManager.me().executeOperationLog(
               businessLog, username, joinPoint, JSON.toJSONString(result));
    }

    /**
     * 操作发生异常记录日志
     */
    @AfterThrowing(pointcut = "getLogPointCut()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);
        String username = getLoginUsername();

        //异步记录日志
        LogManager.me().executeExceptionLog(
                businessLog, username, joinPoint, exception);
    }

    private String getLoginUsername() {
        OAuthUser authUser = LoginContextHolder.me().getLoginUserWithoutException();
        String username = CommonConstant.UNKNOWN;
        if(ObjectUtil.isNotNull(authUser)) {
            username = authUser.getUsername();
        }
        return username;
    }
}
