package com.fyp.hotel.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CustomFilter2 extends OncePerRequestFilter {
    public CustomFilter2() {
        System.out.println("second custom filter is called from constructor");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("second custom filter is called");
        filterChain.doFilter(request, response);
    }
}
