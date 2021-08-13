package com.imcore.admintemplate.security;

import com.imcore.admintemplate.base.consts.CommonConstant;
import com.imcore.admintemplate.base.enums.LogSuccessStatusEnum;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.log.LogManager;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class LoginFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {
    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String username = (String)event.getAuthentication().getPrincipal();
        AuthenticationException exception = event.getException();
        String message = CommonConstant.UNKNOWN;
        if(exception instanceof BadCredentialsException){
            LogManager.me().executeLoginLog(username, LogSuccessStatusEnum.FAIL.getCode(), ResultCodeEnum.PASSWORD_ERROR.getMsg());
        }else if(exception instanceof UsernameNotFoundException){
            LogManager.me().executeLoginLog(username, LogSuccessStatusEnum.FAIL.getCode(), ResultCodeEnum.USER_NO_EXIST.getMsg());
        }else if(exception instanceof DisabledException){
            LogManager.me().executeLoginLog(username, LogSuccessStatusEnum.FAIL.getCode(), ResultCodeEnum.ACCOUNT_DISABLED.getMsg());
        }else{
            LogManager.me().executeLoginLog(username, LogSuccessStatusEnum.FAIL.getCode(), message);
        }
    }


}
