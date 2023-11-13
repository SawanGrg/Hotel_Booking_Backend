package com.fyp.hotel.config;

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
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final UserServiceImplementation userServiceImplementation;
    private final JwtAuthFilter jwtAuthFilter;
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
                .cors(cors -> cors
                        //configurationsource() is used to set the CorsConfigurationSource bean.
                        //CorsConfigurationSource is used to configure the cors filter.
                        .configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(crsf -> crsf
                        .disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/v1/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/vendor/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/user/home").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/user/hotel").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/user/profile").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.PUT, "/v1/user/update-profile").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/v1/user/logout").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/v1/vendor/dashboard").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.POST, "/v1/vendor/addHotelRooms").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/v1/vendor/hotelRooms").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/admin/dashboard").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/dashboard").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling( ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // authenticationProvider() is used to set the AuthenticationProvider bean.
        // it is usefull when we want to use multiple authentication providers.
        // such as DaoAuthenticationProvider, LdapAuthenticationProvider, etc.
        http.authenticationProvider(authenticationProvider());
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
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()s
//                .requestMatchers(HttpMethod.POST, "/v1/login");
//    }


}
