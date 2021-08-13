package com.imcore.admintemplate.base.context.login;
import com.imcore.admintemplate.utils.SpringUtil;

/**
 * 当前登录用户信息获取的接口
 *
 * @author xuyuxiang
 * @date 2020/3/13 12:21
 */
public class LoginContextHolder {

    public static LoginContext me() {
        return SpringUtil.getBean(LoginContext.class);
    }

}
