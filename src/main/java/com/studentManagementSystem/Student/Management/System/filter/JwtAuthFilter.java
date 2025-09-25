package com.studentManagementSystem.Student.Management.System.filter;

import com.studentManagementSystem.Student.Management.System.service.CustomUserDetailsService;
import com.studentManagementSystem.Student.Management.System.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n--- JWT AUTH FILTER ---");
        System.out.println("Processing request to: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
            System.out.println("Token found. Extracted username: " + username);
        } else {
            System.out.println("No Bearer token found in request header.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Loading UserDetails for: " + username);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // THIS IS THE MOST IMPORTANT LOG - IT SHOWS THE USER'S ROLES
            System.out.println("UserDetails loaded. Authorities (Roles): " + userDetails.getAuthorities());

            if (jwtUtil.validateToken(token, userDetails)) {
                System.out.println("Token is valid. Setting authentication in Security Context.");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("Token validation failed.");
            }
        }

        System.out.println("--- FILTER FINISHED ---");
        filterChain.doFilter(request, response);
    }
}

