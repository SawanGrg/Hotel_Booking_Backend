package com.fyp.hotel.config;

import com.fyp.hotel.filter.*;
import com.fyp.hotel.service.user.userImpl.UserServiceImpl;
import com.fyp.hotel.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fyp.hotel.exception.JwtAuthenticationEntryPoint;

import lombok.AllArgsConstructor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig implements WebMvcConfigurer {

    private final TimingFilter timingFilter;
    private final ApiRateLimit apiRateLimit;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserServiceImpl userServiceImplementation;
    @Autowired
    private CorsConfig corsConfig;

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

    // Permit all URLs (no authentication required)
    public static final String[] PERMIT_ALL = {
            "/v1/user/register",
            "/v1/vendor/register",
            "/v1/user/verifyOtp",
            "/v1/user/home",
            "/api/test/upload-video",
            "/v1/user/searchHotel/**",
            "/v1/user/hotel/**",
            "v1/user/hotelReview/**",
            "/v1/user/hotelRooms/**",
            "/images/**",
            "/v1/user/filterRooms/**",
            "v1/user/checkRoomAvailability/**",
            "/v1/user/hotelUserName/**",
            "/v1/user/getInTouch/**",
            "/v1/user/viewBlog",
            "/v1/user/viewBlog/{blogId}",
            "/v1/user/viewPostBlogComment/{blogId}",
            "/v1/user/bookingStatus/**"
    };

    // URLs accessible by ROLE_USER only
    public static final String[] ROLE_USER = {
            "/v1/user/profile",
            "/v1/user/d",
            "v1/user/review/**",
            "/v1/user/postBlog",
            "/v1/user/postBlogComment/{blogId}"
    };

    // URLs accessible by ROLE_VENDOR only
    public static final String[] ROLE_VENDOR = {
            "/v1/vendor/dashboard",
            "/v1/vendor/addHotelRooms",
            "/v1/vendor/hotelRooms",
            "/v1/vendor/report",
            "/v1/vendor/hotelDetails",
            "/v1/vendor/bookings/**",
            "/v1/vendor/roomStatus/**",
            "/v1/vendor/analytics",
            "/v1/vendor/updateRoom/**",
            "/v1/vendor/hotelReview",
            "/v1/vendor/revenue",
            "/v1/vendor/viewRoom/**"
    };

    // URLs accessible by ROLE_ADMIN only
    public static final String[] ROLE_ADMIN = {
            "/v1/admin/dashboard",
            "/vendor/dashboard",
            "/v1/admin/viewAllUsers",
            "/v1/admin/viewAllVendors",
            "/v1/admin/analytics",
            "/v1/admin/viewAllReport",
            "v1/admin/BlogBeforeVerification",
            "v1/admin/verifyBlog/**",
            "/v1/admin/viewAllHotels",
            "/v1/admin/getUserProfile/**",
            "/v1/admin/specificBlog/**",
            "/v1/admin/unverifyUser/**",
            "/v1/admin/unverifyVendor/**",
            "/v1/admin/viewUserMessage"
    };

    // URLs accessible by both ROLE_USER and ROLE_VENDOR
    public static final String[] ROLE_USER_AND_VENDOR = {
            "/v1/user/payment/**",
            "v1/user/user-change-password",
            "/v1/user/view-user-details",
            "/v1/user/update-user-details",
            "/v1/user/viewBookingDetails",
            "/v1/user/khalti/**",
            "/v1/user/khalti/update"
    };


    @Bean
    public SecurityFilterChain permitAllFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(PERMIT_ALL)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add filters that should apply to all requests
        http.addFilterBefore(timingFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiRateLimit, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityFilterChain userFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(ROLE_USER)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().hasAuthority("ROLE_USER")
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtils, userServiceImplementation), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public SecurityFilterChain vendorFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(ROLE_VENDOR)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().hasAuthority("ROLE_VENDOR")
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtils, userServiceImplementation), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(ROLE_ADMIN)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().hasAuthority("ROLE_ADMIN")
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtils, userServiceImplementation), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public SecurityFilterChain userAndVendorFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(ROLE_USER_AND_VENDOR)
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().hasAnyAuthority("ROLE_USER", "ROLE_VENDOR")
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JwtAuthFilter(jwtUtils, userServiceImplementation), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:uploads/images/");

        registry.addResourceHandler("/uploads/videos/**")
                .addResourceLocations("file:uploads/videos/");
    }
}