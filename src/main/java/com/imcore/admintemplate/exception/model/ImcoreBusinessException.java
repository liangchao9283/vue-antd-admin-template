package com.imcore.admintemplate.exception.model;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;

/**
 * 自定义异常类(继承运行时异常)
 *
 * @author 梁超
 * @version 2019/1/21 13:36
 */
public class ImcoreBusinessException extends RuntimeException {

	/**
	 * 系统错误类
	 */
	private ResultCodeEnum resultCodeEnum;

	private Object[] args;

	public ImcoreBusinessException() {
		super();
	}

	public ImcoreBusinessException(ResultCodeEnum resultCodeEnum) {
		this.resultCodeEnum = resultCodeEnum;
	}


    public ImcoreBusinessException(ResultCodeEnum resultCodeEnum, Object... args) {
		this.resultCodeEnum = resultCodeEnum;
        this.args = args;
    }

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}


	public ResultCodeEnum getResultCodeEnum() {
		return resultCodeEnum;
	}

	public void setResultCodeEnum(ResultCodeEnum resultCodeEnum) {
		this.resultCodeEnum = resultCodeEnum;
	}

}
