package com.imcore.admintemplate.base.model;

import com.imcore.admintemplate.base.enums.ResultCodeEnum;

/**
 * 公共响应类
 *
 * @param <T>
 */
public class BaseResponse<T> {

    private Boolean success;

    private String code;

    private String msg;

    private T data;

    private BaseResponse(String code, String msg, T data) {
        if(code.equals(ResultCodeEnum.SUCCESS.getCode())){
            this.setSuccess(true);
        }else{
            this.setSuccess(false);
        }
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(),null);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(ResultCodeEnum.SUCCESS.getCode(), ResultCodeEnum.SUCCESS.getMsg(),data);
    }

    public static <T> BaseResponse<T> error() {
        return new BaseResponse<T>(ResultCodeEnum.SYS_ERROR.getCode(), ResultCodeEnum.SYS_ERROR.getMsg(),null);
    }

    public static <T> BaseResponse<T> error(ResultCodeEnum resultCodeEnum) {
        return new BaseResponse<T>(resultCodeEnum.getCode(), resultCodeEnum.getMsg(),null);
    }

    public static <T> BaseResponse<T> error(ResultCodeEnum resultCodeEnum, T data) {
        return new BaseResponse<T>(resultCodeEnum.getCode(), resultCodeEnum.getMsg(),data);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
