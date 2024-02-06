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

            System.out.println("from class MyCustomFilter step 1.1 hello world --->" + requestURI);

            // Check if the request is for /api/test
            if ("/api/test".equals(requestURI)) {
                System.out.println("from class MyCustomFilter step 2 hello world --->" + servletRequest);
                // Perform actions specific to /api/test
                // For example, you can add your custom logic here

                // Continue the filter chain for all requests
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
