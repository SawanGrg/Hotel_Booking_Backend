package com.fyp.hotel.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyCustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("from class MyCustomFilter step 1 hello world --->" + servletRequest);

        if (servletRequest instanceof HttpServletRequest) {

            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            String requestURI = httpRequest.getRequestURI();

            if ("/api/test".equals(requestURI)) {
                System.out.println("from class MyCustomFilter step 2 hello world --->" + servletRequest);

                filterChain.doFilter(servletRequest, servletResponse);
            }
            else
            {
                System.out.println("from class MyCustomFilter step 3 hello world --->" + servletRequest);
                filterChain.doFilter(servletRequest, servletResponse);
            }

        }
    }
}
