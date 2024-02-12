package com.fyp.hotel.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;
import com.fyp.hotel.util.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;
    private UserServiceImplementation userServiceImplementation;

    public JwtAuthFilter(JwtUtils jwtUtils, UserServiceImplementation userServiceImplementation) {
        this.jwtUtils = jwtUtils;
        this.userServiceImplementation = userServiceImplementation;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String userName = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            userName = jwtUtils.extractUsername(jwtToken);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userServiceImplementation.loadUserByUsername(userName);

            if (jwtToken != null && jwtUtils.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                // Log user details and authorities
                System.out.println("User details: " + userDetails);
                System.out.println("User authorities: " + userDetails.getAuthorities());
                System.out.println("jwt authentication is done successfully");
                filterChain.doFilter(request, response);
                return;
            }
        }

//        if (jwtToken == null ) {
//            System.out.println("jwt token is null");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Unauthorized");
//            return;
//        }

        filterChain.doFilter(request, response);
    }

}
