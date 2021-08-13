package com.imcore.admintemplate.base.config;

import com.imcore.admintemplate.base.aop.BusinessLogAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 切面配置
 *
 * @author liangchao
 * @date 2021/8/7 11:25
 */
@Configuration
public class AopConfig {
    /**
     * 日志切面
     */
    @Bean
    public BusinessLogAop businessLogAop() {
        return new BusinessLogAop();
    }
}
