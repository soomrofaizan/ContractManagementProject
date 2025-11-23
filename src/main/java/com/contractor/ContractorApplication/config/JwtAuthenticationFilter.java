package com.contractor.ContractorApplication.config;

import com.contractor.ContractorApplication.services.UserService;
import com.contractor.ContractorApplication.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String mobileNumber;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            mobileNumber = jwtUtil.extractMobileNumber(jwt);
            Long userId = jwtUtil.extractUserId(jwt);

            if (mobileNumber != null && userId != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtUtil.validateToken(jwt)) {
                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(mobileNumber, null, null);

                    // Store user details for easy access in controllers
                    Map<String, Object> details = new HashMap<>();
                    details.put("userId", userId);
                    details.put("mobileNumber", mobileNumber);
                    authToken.setDetails(details);

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token is invalid - continue without authentication
        }

        filterChain.doFilter(request, response);
    }
}
