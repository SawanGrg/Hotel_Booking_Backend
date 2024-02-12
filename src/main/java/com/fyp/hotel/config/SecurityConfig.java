package com.fyp.hotel.config;

//import com.fyp.hotel.filter.MyCustomFilter;
import com.fyp.hotel.filter.*;
import com.fyp.hotel.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fyp.hotel.exception.JwtAuthenticationEntryPoint;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity // @EnableWebSecurity is used to enable web security in a project.
@AllArgsConstructor
@EnableMethodSecurity // @EnableMethodSecurity is used to enable method level security based on annotations.
public class SecurityConfig {

    private final TimingFilter timingFilter;
    private final ApiRateLimit apiRateLimit;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserServiceImplementation userServiceImplementation;

    //    private final UserServiceImplementation userServiceImplementation;
//    private final MyCustomFilter myCustomFilter;
//    private final JwtAuthFilter jwtAuthFilter;
//    @Bean
//    public JwtAuthFilter jwtAuthFilter() {
//        return new JwtAuthFilter(jwtUtils, userServiceImplementation);
//    }

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

    /*
    using @component makes the filter to be called for all the request as it is registered as a bean
    it is register in default filter chain of spring security
    but we remove @component
    and make the filter to be called for specific request for fine grained control
     */
//    for open api like get all hotels, get all rooms, get all images
    @Bean
    public SecurityFilterChain apiTestFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatchers(matchers -> matchers
                        .requestMatchers("/api/test/**")
                        .requestMatchers("/v1/user/hotel")
                )

                // Configure other security aspects as needed
                .addFilterBefore(new MyCustomFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomFilter2(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore( new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }

//for all other request which needs authentication and authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(crsf -> crsf
                        .disable())
                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers(HttpMethod.POST, "/v1/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/vendor/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/user/verifyOtp").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/user/home").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/user/hotel").permitAll()
//                        for dynamic url
                        .requestMatchers(HttpMethod.GET, "/v1/user/hotelRooms/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/user/filterRooms/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/v1/user/payment/**").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/v1/user/profile").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "v1/user/user-change-password").hasAuthority("ROLE_USER")

                        .requestMatchers(HttpMethod.GET, "/v1/user/view-user-details").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/v1/user/update-user-details").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/v1/user/d").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/v1/vendor/dashboard").hasAnyAuthority("ROLE_VENDOR")

                        .requestMatchers(HttpMethod.POST, "/v1/vendor/addHotelRooms").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/v1/vendor/hotelRooms").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.POST, "/v1/vendor/report").hasAnyAuthority("ROLE_VENDOR")

//                        admin url
                        .requestMatchers(HttpMethod.GET, "/v1/admin/dashboard").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/dashboard").hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/test").hasAuthority("ROLE_USER")

                        .anyRequest().permitAll())
                .exceptionHandling( ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(apiRateLimit, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(timingFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtAuthFilter(jwtUtils, userServiceImplementation), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config); //registerCorsConfiguration("/**", config) means that all the endpoints in the application will have the CORS filter applied to them.
        return new CorsFilter(source);
    }
}
