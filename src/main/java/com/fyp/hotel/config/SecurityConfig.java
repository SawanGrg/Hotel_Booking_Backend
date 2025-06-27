package com.fyp.hotel.config;

import com.fyp.hotel.filter.*;
import com.fyp.hotel.util.JwtUtils;
import com.fyp.hotel.exception.JwtAuthenticationEntryPoint;
import com.fyp.hotel.service.user.userImpl.UserServiceImplementation;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final TimingFilter timingFilter;
    private final ApiRateLimit apiRateLimit;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserServiceImplementation userServiceImplementation;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServiceImplementation);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(getPublicEndpoints()).permitAll()
                        .requestMatchers(HttpMethod.POST, getUserProtectedEndpoints()).hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, getVendorProtectedEndpoints()).hasAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, getAdminProtectedEndpoints()).hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(apiRateLimit, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(timingFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtAuthFilter(jwtUtils, userServiceImplementation), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private String[] getPublicEndpoints() {
        return new String[]{
                "/v1/user/register",
                "/v1/vendor/register",
                "/v1/user/verifyOtp",
                "/v1/user/home",
                "/api/test/upload-video",
                "/v1/user/searchHotel/**",
                "/v1/user/hotel/**",
                "/v1/user/hotelRooms/**",
                "/images/**",
                "/v1/user/filterRooms/**",
                "/v1/user/checkRoomAvailability/**",
                "/v1/user/hotelUserName/**",
                "/v1/user/getInTouch/**",
                "/v1/user/viewBlog",
                "/v1/user/viewBlog/{blogId}",
                "/v1/user/viewPostBlogComment/{blogId}",
                "/v1/user/bookingStatus/**"
        };
    }

    private String[] getUserProtectedEndpoints() {
        return new String[]{
                "/v1/user/payment/**",
                "/v1/user/profile",
                "/v1/user/user-change-password",
                "/v1/user/view-user-details",
                "/v1/user/update-user-details",
                "/v1/user/d",
                "/v1/user/review/**",
                "/v1/user/postBlog",
                "/v1/user/postBlogComment/{blogId}",
                "/v1/user/viewBookingDetails",
                "/v1/user/khalti/**",
                "/v1/user/khalti/update"
        };
    }

    private String[] getVendorProtectedEndpoints() {
        return new String[]{
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
    }

    private String[] getAdminProtectedEndpoints() {
        return new String[]{
                "/v1/admin/dashboard",
                "/vendor/dashboard",
                "/v1/admin/viewAllUsers",
                "/v1/admin/viewAllVendors",
                "/v1/admin/analytics",
                "/v1/admin/viewAllReport",
                "/v1/admin/BlogBeforeVerification",
                "/v1/admin/verifyBlog/**",
                "/v1/admin/viewAllHotels",
                "/v1/admin/getUserProfile/**",
                "/v1/admin/specificBlog/**",
                "/v1/admin/unverifyUser/**",
                "/v1/admin/unverifyVendor/**",
                "/v1/admin/viewUserMessage"
        };
    }
}
