package com.imcore.admintemplate.security;

import com.imcore.admintemplate.filter.OptionsRequestFilter;
import com.imcore.admintemplate.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * spring security入口配置类
 * liangchao 2021/7/8
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled=true,jsr250Enabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**获取用户信息的service*/
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**登录成功的回调*/
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    /**登录失败的回调*/
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    /**匿名用户访问不需要登录的接口,但是无权限时的失败处理类*/
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**已认证过的用户访问无权限的接口时失败处理类*/
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**token校验成功后刷新处理类*/
    @Autowired
    private JwtTokenRefreshHandler jwtTokenRefreshHandler;

    /**拦截OPTIONS请求，直接返回header*/
    @Autowired
    private OptionsRequestFilter optionsRequestFilter;

    /**登出时清除登录信息的处理类*/
    @Autowired
    private TokenClearLogoutHandler tokenClearLogoutHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //设置hideUserNotFoundExceptions为false
        provider.setHideUserNotFoundExceptions(false);
        //userDetailsService是你自己的userDetailsService实现类，
        //UsernameNotFoundException就是其中抛出来的
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**校验token方法的提供类*/
    @Bean("jwtAuthenticationProvider")
    protected AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(userDetailsService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                 // 如果有允许匿名的url，填在antMatchers括号内
                .authorizeRequests()
                    .antMatchers().permitAll()
                    // 除上述antMatchers外的所有请求全部需要鉴权认证
                    .anyRequest().authenticated()
                .and()
                .sessionManagement().disable()
                // 由于使用的是JWT，我们这里不需要csrf
                .cors().and()
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                        new Header("Access-control-Allow-Origin","*"),
                        //使ajax请求能够取到header中的jwt token信息
                        new Header("Access-Control-Expose-Headers","Authorization"))))
                .and()
                //禁用跨域
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy()).sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(optionsRequestFilter, CorsFilter.class)
                //添加登录filter,设置登录成功回调和登录失败的回调
                .apply(new JsonLoginConfigurer<>()).loginSuccessHandler(loginSuccessHandler).loginFailureHandler(loginFailureHandler)
               // .formLogin().successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and()
                //添加token的filter,校验请求是否携带token,以及token是否合法,同时设置了特例请求"/logout",logout接口必须携带token但是token是错误也可以通过过滤器
                .apply(new JwtLoginConfigurer<>())
                    .tokenValidSuccessHandler(jwtTokenRefreshHandler)
                    .tokenValidFailureHandler(loginFailureHandler)
                    .permissiveRequestUrls("/logout","/login")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler).and()
                .logout()
                    .addLogoutHandler(tokenClearLogoutHandler)
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                // 自定义登陆用户名和密码参数，默认为username和password
                //.usernameParameter("username")
                //.passwordParameter("password")
                ;

        // 禁用缓存
        http.headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }
}
