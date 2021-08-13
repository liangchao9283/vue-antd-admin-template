package com.imcore.admintemplate.security;

import com.alibaba.fastjson.JSONObject;
import com.imcore.admintemplate.base.enums.LogSuccessStatusEnum;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.base.model.BaseToken;
import com.imcore.admintemplate.log.LogManager;
import com.imcore.admintemplate.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        UserDetails user = (UserDetails) authentication.getPrincipal();
        BaseToken baseToken = userDetailsService.saveUserLoginInfo(user);
        response.setHeader("Authorization", JSONObject.toJSONString(baseToken));

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String result = JSONObject.toJSONString(BaseResponse.success());

        LogManager.me().executeLoginLog(user.getUsername(), LogSuccessStatusEnum.SUCCESS.getCode(), null);


        printWriter.write(result);
        printWriter.flush();
        printWriter.close();
    }
}
