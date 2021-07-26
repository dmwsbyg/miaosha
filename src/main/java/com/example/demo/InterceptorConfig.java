package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
//将拦截器加入到配置中

    @Override
    public void  addInterceptors(InterceptorRegistry registry){

        //添加被拦截的路径  这里是指所有路径都被拦截
        registry.addInterceptor(new InterceptorDemo()).addPathPatterns("/**");

        //添加不拦截的路径
//        registry.addInterceptor(new InterceptorDemo()).excludePathPatterns("/userLogin","/css/**","/images/**","/js/**","/login.html");

//        registry.addInterceptor(new InterceptorDemo()).excludePathPatterns("http://127.0.0.1:8080/user/getotp","/register.html");
    }
}
