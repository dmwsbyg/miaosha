package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class InterceptorDemo implements HandlerInterceptor {
//实现拦截接口

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)throws Exception{
       String origin = httpServletRequest.getHeader("Origin");
       httpServletResponse.setContentType("application/json;charset=UTF-8");
       httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
       httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//       httpServletResponse.setHeader("Access-Control-Max-Age", "1800");
//       httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
        httpServletResponse.setHeader("Access-Control-Allow-Credentials","true");

        StringBuffer requestURL = httpServletRequest.getRequestURL();
        System.out.println("前置拦截器1 preHandle：请求的url为"+requestURL.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)throws Exception{
        System.out.println("拦截器1 postHandle：");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,Object o,Exception e)throws Exception{
        System.out.println("拦截器1 afterCompletion：");
    }
}
