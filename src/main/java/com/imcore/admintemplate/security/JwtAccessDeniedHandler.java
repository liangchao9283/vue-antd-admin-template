package com.imcore.admintemplate.security;

import com.alibaba.fastjson.JSONObject;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**认证过的用户访问无权限资源时的异常
 * @author: liangchao
 * createAt: 2018/9/21
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //登陆状态下，权限不足执行该方法

        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = JSONObject.toJSONString(BaseResponse.error(ResultCodeEnum.PERMISSION_ERROR));
        printWriter.write(body);
        printWriter.flush();
    }
}
