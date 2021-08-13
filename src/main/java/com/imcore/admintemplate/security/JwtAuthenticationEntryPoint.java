package com.imcore.admintemplate.security;


import com.alibaba.fastjson.JSONObject;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: liangchao
 * createAt: 2021/7/8
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(403);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONObject.toJSONString(BaseResponse.error(ResultCodeEnum.NO_LOGIN)));
        printWriter.flush();
    }
}
