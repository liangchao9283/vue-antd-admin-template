package com.imcore.admintemplate.base.enums;


/***
 * @Author: 梁超
 * @Date: 2021/7/8 14:07
 * @Description: 错误状态码
 */
public enum ResultCodeEnum {

    // 公共
    SUCCESS ("200","SUCCESS"),
    SYS_ERROR("1","系统错误!"),
    PARAMS_MISS ("2","参数缺失!"),
    PARAMS_ERROR ("3","参数错误!"),
    DB_DATA_NO_EXISTS ("4","你操作的数据不存在!"),
    REQUEST_EMPTY("5", "当前请求参数为空或数据缺失，请联系管理员"),
    REQUEST_PARAMS_FORMAT_ERROR("6", "请求参数格式不正确，请检查参数格式"),
    REQUEST_MEDIA_TYPE_ERROR("7", "媒体类型不支持，请检查请求头中的Content-Type"),
    /**
     * 不支持该请求方法，请求方法应为POST
     */
    REQUEST_METHOD_IS_POST("8", "该请求不支持POST方法"),

    /**
     * 不支持该请求方法，请求方法应为GET
     */
    REQUEST_METHOD_IS_GET("9", "该请求不支持GET方法"),
    REQUEST_METHOD_ERROR("10", "请求方法不正确"),
    URL_NOT_EXIST("11", "资源路径不存在，请检查请求地址"),

    //登录权限相关
    LOGIN_FAIL("1000","登录失败!"),
    NO_LOGIN ("1001","无权访问,请先登录!"),
    LOGIN_STATUS_EXPIRED ("1002","登录超时，请重新登录!"),
    USER_NO_EXIST("1003","账户不存在!"),
    PASSWORD_ERROR("1004","密码不正确!"),
    PERMISSION_ERROR("1005","权限不足或接口权限调整了,无法获取数据!请联系管理员"),
    ACCOUNT_DISABLED("1006","账户被禁用了!"),
    BAD_TOKEN("1007","错误的凭证!"),
    NO_LOGIN_USER("1008","无登录用户"),

    //菜单相关
    MENU_PERMISSION_CODE_EXISTS("1101","权限值存在!"),
    MENU_COMPONENT_NAME_SYSTEM_SYSMENU_CANNOT_ADD("1102","system/sysMenu组件不允许添加进菜单中!"),
    MENU_COMPONENT_NAME_RULE_ERROR("1103","菜单组件名不能以/或pages开头"),
    MENU_PATH_RULE_ERROR("1104","菜单路由名不能以/开头"),

    //角色相关
    ROLE_SUPER_ADMIN_NOT_ALLOW_OPERATE("1201","超级管理员是系统预定义的角色,不允许添加,编辑或删除"),
    ROLE_SUPER_ADMIN_CODE_EXIST("1202","super_admin是系统预定义角色编号,不允许添加或编辑"),
    ROLE_CODE_REPETITIVE("1203","角色编号已存在,角色编号必须全局唯一"),
    ROLE_CODE_PREFIX_ERROR("1204","角色编号不能以ROLE_开头"),

    //用户相关
    USER_USERNAME_EXIST("1301","此账号已存在,请勿重复添加!"),
    USER_PHONE_EXIST("1302","手机号已绑定过账号,请勿重复添加!"),
    USER_SUPER_ADMIN_NOT_ALLOW_OPERATE("1303","superAdmin是系统预定义的用户,不允许操作"),
    USER_OLD_PASSWORD_ERROR("1304","原密码不正确"),
    ;
    private String code;
    private String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ResultCodeEnum buildErrorMsg(String errorMsg){
        ResultCodeEnum resultCodeEnum = ResultCodeEnum.SYS_ERROR;
        resultCodeEnum.setMsg(errorMsg);
        return resultCodeEnum;
    }

}
