package com.imcore.admintemplate.exception.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 在com.imcore.admintemplate.exception.MyExceptionHandle中配置@ExceptionHandler注解,该注解会捕获全局异常,且优先捕获
 * 导致com.imcore.admintemplate.security.JwtAccessDeniedHandler类无法捕获AccessDeniedException异常
 * 所以在本类中会捕获AccessDeniedException并抛出,这样JwtAccessDeniedHandler就能正常工作了
 */
@ControllerAdvice
public class AccessDeniedExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public void accessDeniedException(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }
}
