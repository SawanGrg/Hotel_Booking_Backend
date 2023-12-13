package com.fyp.hotel.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class TimingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String url = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        log.info("Time taken to execute {} {} {} is {} ms", method, url, queryString != null ? "?" + queryString : "", duration);
    }
}
