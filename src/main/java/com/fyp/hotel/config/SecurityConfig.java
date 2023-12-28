package com.fyp.hotel.config;

//import com.fyp.hotel.filter.MyCustomFilter;
import com.fyp.hotel.filter.ApiRateLimit;
import com.fyp.hotel.filter.TimingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fyp.hotel.exception.JwtAuthenticationEntryPoint;
import com.fyp.hotel.filter.JwtAuthFilter;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity // @EnableWebSecurity is used to enable web security in a project.
@AllArgsConstructor
@EnableMethodSecurity // @EnableMethodSecurity is used to enable method level security based on annotations.
public class SecurityConfig {

    private final UserServiceImplementation userServiceImplementation;
    private final JwtAuthFilter jwtAuthFilter;
    private final TimingFilter timingFilter;
    private final ApiRateLimit apiRateLimit;
    // private final CookieFilter cookieFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServiceImplementation);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                securityMatcher() is used to set the RequestMatcher bean where requestMatchers bean means the request is matched against the provided matchers.
//                 it is usefull when we want to use multiple request matchers.
//                 .securityMatcher(request -> request.getServletPath().startsWith("/v1")
//                        || request.getServletPath().startsWith("/admin")
//                        || request.getServletPath().startsWith("/vendor")
//                        || request.getServletPath().startsWith("/user")
//
//                )
//                .securityMatcher("/api/test")
//                .addFilterBefore(new MyCustomFilter(), UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors
                        .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(crsf -> crsf
                        .disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/v1/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/vendor/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/user/home").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/user/hotel").permitAll()
//                        for dynamic url
                        .requestMatchers(HttpMethod.GET, "/v1/user/hotelRooms/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/user/payment/**").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/v1/user/profile").hasAuthority("ROLE_USER")

                        .requestMatchers(HttpMethod.GET, "/v1/user/view").hasAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.PUT, "/v1/user/update-profile").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/v1/user/logout").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/v1/vendor/dashboard").hasAnyAuthority("ROLE_VENDOR")

                        .requestMatchers(HttpMethod.POST, "/v1/vendor/addHotelRooms").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/v1/vendor/hotelRooms").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/admin/dashboard").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/dashboard").hasAuthority("ROLE_ADMIN")

//                        .requestMatchers(HttpMethod.GET,"/api/test").hasAuthority("ROLE_USER")

                        .anyRequest().permitAll())
                .exceptionHandling( ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // authenticationProvider() is used to set the AuthenticationProvider bean.
        // it is usefull when we want to use multiple authentication providers.
        // such as DaoAuthenticationProvider, LdapAuthenticationProvider, etc.
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(apiRateLimit, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(timingFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(cookieFilter, JwtAuthFilter.class);

        return http.build();
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
//        config.addAllowedMethod("GET");
//        config.addAllowedMethod("POST");
//        config.addAllowedMethod("PUT");
//        config.addAllowedMethod("DELETE"); // Allow DELETE method
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config); //registerCorsConfiguration("/**", config) means that all the endpoints in the application will have the CORS filter applied to them.
        return new CorsFilter(source);
    }

}
