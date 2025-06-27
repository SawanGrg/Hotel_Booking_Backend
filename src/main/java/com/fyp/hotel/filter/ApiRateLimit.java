package com.fyp.hotel.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ApiRateLimit extends OncePerRequestFilter {

    private static final Map<String, Map<Long, Integer>> ipAddressMap = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_IP = 400;
    private static final int WINDOW_SIZE_SECONDS = 10;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String userIpAddress = getClientIP(request);
        long currentTimeSeconds = Instant.now().getEpochSecond(); // Get current time in seconds
        Map<Long, Integer> requestCounts = ipAddressMap.computeIfAbsent(userIpAddress, k -> new ConcurrentHashMap<>());

        // Remove entries older than the window size
        requestCounts.entrySet().removeIf(entry ->
                Duration.between(Instant.ofEpochSecond(entry.getKey()), Instant.ofEpochSecond(currentTimeSeconds))
                        .getSeconds() >= WINDOW_SIZE_SECONDS);

        int count = requestCounts.values().stream().mapToInt(Integer::intValue).sum();

        if (count >= MAX_REQUESTS_PER_IP) {
            response.sendError(429, "Too many requests");
            return;
        }

        requestCounts.put(currentTimeSeconds, requestCounts.getOrDefault(currentTimeSeconds, 0) + 1);
        filterChain.doFilter(request, response);
    }


    private String getClientIP(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
