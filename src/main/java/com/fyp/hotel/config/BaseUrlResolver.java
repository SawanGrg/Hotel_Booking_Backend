package com.fyp.hotel.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class BaseUrlResolver {

    private final HttpServletRequest request;

    public BaseUrlResolver(HttpServletRequest request) {
        this.request = request;
    }

    public String getBaseUrl() {
        String scheme = request.getHeader("X-Forwarded-Proto");
        String serverName = request.getHeader("X-Forwarded-Host");
        int port = getPortFromHeader(request.getHeader("X-Forwarded-Port"));
        String contextPath = request.getContextPath();

        if (scheme == null) {
            scheme = request.getScheme();
        }

        if (serverName == null) {
            serverName = request.getServerName();
        }

        if (port == -1) {
            port = request.getServerPort();
        }

        // Only include the port in the base URL if it is not 80 or 443
        if (port == 80 || port == 443) {
            return scheme + "://" + serverName + contextPath;
        } else {
            return scheme + "://" + serverName + ":" + port + contextPath;
        }
    }

    private int getPortFromHeader(String headerValue) {
        if (headerValue != null) {
            try {
                return Integer.parseInt(headerValue);
            } catch (NumberFormatException e) {
                // ignore and fall back to default port
            }
        }
        return -1; // return -1 to indicate that port is not specified in header
    }
}
