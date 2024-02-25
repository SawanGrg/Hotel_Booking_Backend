package com.fyp.hotel.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
//@WebFilter("/api/test") //web filter is used to intercept the request and its response. It is used to perform operations on the request and response before sending to the actual server or after sending the response to the client.
public class MyCustomFilter extends OncePerRequestFilter {

    public MyCustomFilter() {
        System.out.println("My custom filter is called from constructor");
    }

//    private final RequestMatcher requestMatcher =  new AntPathRequestMatcher("/api/test", "GET");
//
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        RequestMatcher matchers = new NegatedRequestMatcher(requestMatcher);
//        return matchers.matches(request); // apply the filter when the request does not match the requestMatcher
//    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("My custom filter is called from doFilterInternal");
        filterChain.doFilter(request, response);
    }


}
