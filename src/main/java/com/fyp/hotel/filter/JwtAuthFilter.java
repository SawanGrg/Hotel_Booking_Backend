package com.fyp.hotel.filter;

import java.io.IOException;

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

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    
    private final JwtUtils jwtUtils;
    private final UserServiceImplementation userServiceImplementation;

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

    final String authorizationHeader = request.getHeader("AUTHORIZATION");
    final String jwtToken;
    final String userName;

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
        filterChain.doFilter(request, response);
        return;
    }

    jwtToken = authorizationHeader.substring(7);
    userName = jwtUtils.extractUsername(jwtToken);

    System.out.println("jwtToken: from class userservice implementation step 1" + jwtToken);
    
    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        
        
        System.out.println("from class userservice implementation step 2" + jwtToken);
        
        UserDetails userDetails = userServiceImplementation.loadUserByUsername(userName);
        
        System.out.println("from class userservice implementation step 3 ----->" + userDetails.toString());
        
        if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken userNamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            userNamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuthenticationToken);

            // Log user details and authorities
            System.out.println("User details: " + userDetails);
            System.out.println("User authorities: " + userDetails.getAuthorities());
            System.out.println("jwt authentication is done successfully");
        }
    }

    filterChain.doFilter(request, response);


    }
    
}
