package com.imcore.admintemplate.base.consts;

public interface CommonConstant {
    String DEFAULT_PASSWORD = "Abc123456,.";
    String DEFAULT_USER_ADMIN_USERNAME = "superAdmin";
    String DEFAULT_USER_ADMIN_NAME = "超级管理员";
    String DEFAULT_ROLE_ADMIN_NAME = "超级管理员";
    String DEFAULT_ROLE_ADMIN_CODE = "super_admin";

    String MENU_TOP_LEVEL_ID = "0";  //菜单最顶级的id为0
    Integer MENU_TYPE_DIRECTORY = 0;   //菜单类型,0目录
    Integer MENU_TYPE_PAGE = 1;   //菜单类型,1页面
    Integer MENU_TYPE_BUTTON = 2;   //菜单类型,2按钮

    Integer USER_STATUS_ENABLE = 1;     //用户状态-正常
    Integer USER_STATUS_DISABLE = 2;    //用户状态-禁用

    /**
     * 未知标识
     */
    String UNKNOWN = "Unknown";

    /**
     * 用户代理
     */
    String USER_AGENT = "User-Agent";
}
