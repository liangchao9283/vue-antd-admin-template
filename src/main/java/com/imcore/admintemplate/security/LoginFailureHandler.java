package com.imcore.admintemplate.security;

import com.alibaba.fastjson.JSONObject;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException{
		//response.setStatus(HttpStatus.UNAUTHORIZED.value());

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		String result = "";
		if(exception instanceof NonceExpiredException){
			response.setStatus(403);
			result  = JSONObject.toJSONString(BaseResponse.error(ResultCodeEnum.LOGIN_STATUS_EXPIRED));
		}else if(exception instanceof BadCredentialsException){
			result  = JSONObject.toJSONString(BaseResponse.error(ResultCodeEnum.PASSWORD_ERROR));
		}else if(exception instanceof InsufficientAuthenticationException){
			response.setStatus(403);
			result  = JSONObject.toJSONString(BaseResponse.error(ResultCodeEnum.BAD_TOKEN));
		}else if(exception instanceof UsernameNotFoundException){
			result  = JSONObject.toJSONString(BaseResponse.error(ResultCodeEnum.USER_NO_EXIST));
		}else if(exception instanceof DisabledException){
			result  = JSONObject.toJSONString(BaseResponse.error(ResultCodeEnum.ACCOUNT_DISABLED));
		}else{
			result = JSONObject.toJSONString(BaseResponse.error(ResultCodeEnum.LOGIN_FAIL));
		}

		printWriter.write(result);
		printWriter.flush();
		printWriter.close();
	}

}
