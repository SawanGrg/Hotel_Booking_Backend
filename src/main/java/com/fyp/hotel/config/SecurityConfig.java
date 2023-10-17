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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fyp.hotel.exception.JwtAuthenticationEntryPoint;
import com.fyp.hotel.filter.JwtAuthFilter;
import com.fyp.hotel.serviceImpl.user.UserServiceImplementation;

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
                .csrf(crsf -> crsf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/vendor/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/home").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/profile").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.GET, "/vendor/dashboard").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.POST, "vendor/addhotelroom").hasAnyAuthority("ROLE_VENDOR")
                        .requestMatchers(HttpMethod.GET, "/admin/dashboard").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/vendor/dashboard").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling( ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // authenticationProvider() is used to set the AuthenticationProvider bean.
        // it is usefull when we want to use multiple authentication providers.
        // such as DaoAuthenticationProvider, LdapAuthenticationProvider, etc.
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(cookieFilter, JwtAuthFilter.class);

        return http.build();

    }

}
