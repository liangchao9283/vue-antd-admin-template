package com.imcore.admintemplate.exception.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.imcore.admintemplate.base.consts.SymbolConstant;
import com.imcore.admintemplate.base.model.BaseResponse;
import com.imcore.admintemplate.base.enums.ResultCodeEnum;
import com.imcore.admintemplate.exception.model.ImcoreBusinessException;
import com.imcore.admintemplate.utils.HttpServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@ControllerAdvice
public class MyExceptionHandler {


    /**
     * 请求参数缺失异常
     *
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public BaseResponse missParamException(MissingServletRequestParameterException e) {
        log.error(">>> 请求参数缺失异常，具体信息为：{}", e.getMessage());
        String parameterType = e.getParameterType();
        String parameterName = e.getParameterName();
        String message = StrUtil.format(">>> 缺少请求的参数{}，类型为{}", parameterName, parameterType);
        return BaseResponse.error(ResultCodeEnum.buildErrorMsg(message));
    }


    /**
     * 拦截参数格式传递异常(比如有个参数size为字符串，但是前端传了布尔值，那么就会进入此异常)
     *
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public BaseResponse httpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error(">>> 参数格式传递异常，具体信息为：{}",  e.getMessage());
        return BaseResponse.error(ResultCodeEnum.REQUEST_PARAMS_FORMAT_ERROR);
    }

    /**
     * 拦截不支持媒体类型异常，比如某接口要求使用application/json格式传参，如果使用application/x-www-form-urlencoded格式就会进入此异常
     *
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public BaseResponse httpMediaTypeNotSupport(HttpMediaTypeNotSupportedException e) {
        log.error(">>> 媒体类型异常，具体信息为：{}", e.getMessage());
        return BaseResponse.error(ResultCodeEnum.REQUEST_MEDIA_TYPE_ERROR);
    }

    /**
     * 拦截请求方法的异常
     *
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public BaseResponse methodNotSupport(HttpServletRequest request) {
        if (ServletUtil.isPostMethod(request)) {
            log.error(">>> 请求方法异常，具体信息为：{}", ResultCodeEnum.REQUEST_METHOD_IS_POST.getMsg());
            return BaseResponse.error(ResultCodeEnum.REQUEST_METHOD_IS_POST);
        } else if (ServletUtil.isGetMethod(request)) {
            log.error(">>> 请求方法异常，具体信息为：{}",  ResultCodeEnum.REQUEST_METHOD_IS_GET.getMsg());
            return BaseResponse.error(ResultCodeEnum.REQUEST_METHOD_IS_GET);
        }else{
            String message = "请求方法不正确，当前错误的请求方法为:"+request.getMethod();
            log.error(">>> 请求方法异常，具体信息为：{}",  message);
            return BaseResponse.error(ResultCodeEnum.buildErrorMsg(message));
        }
    }


    /**
     * 拦截资源找不到的运行时异常
     *
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public BaseResponse notFound(NoHandlerFoundException e) {
        log.error(">>> 资源不存在异常，具体信息为：{}", e.getMessage() +"，请求地址为:" + HttpServletUtil.getRequest().getRequestURI());
        return BaseResponse.error(ResultCodeEnum.URL_NOT_EXIST);
    }

    /**
     * 拦截参数校验错误异常,JSON传参
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String argNotValidMessage = getArgNotValidMessage(e.getBindingResult());
        log.error(">>> 参数校验错误异常，具体信息为：{}", argNotValidMessage);
        return BaseResponse.error(ResultCodeEnum.buildErrorMsg(argNotValidMessage));
    }

    /**
     * 拦截业务异常
     *
     */
    @ExceptionHandler(ImcoreBusinessException.class)
    @ResponseBody
    public BaseResponse businessError(ImcoreBusinessException e) {
        log.error(">>> 业务异常，具体信息为：{}",  e.getResultCodeEnum().getMsg());
        return BaseResponse.error(e.getResultCodeEnum());
    }

    /**
     * 拦截未知的运行时异常
     *
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public BaseResponse handle(Throwable e) {
        e.printStackTrace();
        return BaseResponse.error(ResultCodeEnum.SYS_ERROR);
    }

    /**
     * 获取请求参数不正确的提示信息
     * <p>
     * 多个信息，拼接成用逗号分隔的形式
     *
     */
    private String getArgNotValidMessage(BindingResult bindingResult) {
        if (bindingResult == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();

        //多个错误用逗号分隔
        List<FieldError> allErrorInfos = bindingResult.getFieldErrors();
        for (FieldError error : allErrorInfos) {
            stringBuilder.append(SymbolConstant.COMMA).append(error.getField()).append(SymbolConstant.COLON).append(error.getDefaultMessage());
        }

        //最终把首部的逗号去掉
        return StrUtil.removePrefix(stringBuilder.toString(), SymbolConstant.COMMA);
    }

}
