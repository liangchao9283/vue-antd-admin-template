package com.imcore.admintemplate.base.enums;

import lombok.Getter;

@Getter
public enum VisLogTypeEnum {

    /**
     * 登录日志
     */
    LOGIN(1, "登录"),

    /**
     * 退出日志
     */
    EXIT(2, "登出");

    private final Integer code;

    private final String message;

    VisLogTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
