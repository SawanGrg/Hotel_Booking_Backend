// package com.fyp.hotel.filter;
//
// import java.io.IOException;
//
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
//
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
//
// @Component
// public class CookieFilter extends OncePerRequestFilter {
//
//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//             throws ServletException, IOException {
//
//                 System.out.println("from cookie filter after JWT authentication --->" + request);
//                 filterChain.doFilter(request, response);
//
//         System.out.println("from cookie filter from after JWT authentication --->" + request);
//         HttpServletRequest req = (HttpServletRequest) request;
//         HttpServletResponse res = (HttpServletResponse) response;
//
//         System.out.println("from class UserServiceImplementation step 9  --->" + req);
//
//         // Extract cookies from the request
//         Cookie[] cookies = req.getCookies();
//         System.out.println("from class UserServiceImplementation step 10 --->" + req);
//
//         boolean cookieFound = false; // Flag to check if the cookie is found
//         System.out.println("from class UserServiceImplementation step 11 --->" + req);
//         if (cookies != null) {
//             System.out.println("from class UserServiceImplementation step 12 --->" + req);
//             for (Cookie cookie : cookies) {
//                 System.out.println("from class UserServiceImplementation step 13 --->" + req);
//                 if (cookie.getName().equals("user_cookie")) {
//                     cookieFound = true;
//                     System.out.println("Cookie filter successful");
//                     break; // Exit the loop if the cookie is found
//                 }
//             }
//         }
//
//         if (cookieFound) {
//             // Cookie found, continue with the request
//             filterChain.doFilter(req, res);
//         } else {
//             System.out.println("from class UserServiceImplementation step 14 --->" + req);
//             // No cookies found, block the request (you can handle this as needed)
//             res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
//         }
//     }
// }
//
