package com.example.demo;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
//public class CorsFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        String origin = request.getHeader("origin");
////        String source = servletRequest.getHea
//        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Origin", origin); //允许访问的Origin（请求源）
//        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Headers","*");// 添加请求头复杂请求导致跨域的解决方法
//        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Credentials","true");// 添加请求头复杂请求导致跨域的解决方法
//        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Methods","GET,PUT,DELETE,POST");// 添加请求头复杂请求导致跨域的解决方法
////
//        System.out.println(((HttpServletRequest)servletRequest). getMethod());
//        filterChain.doFilter(servletRequest,servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
