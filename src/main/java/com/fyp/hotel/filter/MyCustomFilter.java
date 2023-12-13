//package com.fyp.hotel.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class MyCustomFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println(
//                "from class MyCustomFilter step 1 --->" + servletRequest);
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}