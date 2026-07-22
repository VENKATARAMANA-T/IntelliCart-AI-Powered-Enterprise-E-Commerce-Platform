package com.example.Product_Service.config;

import com.example.Product_Service.security.JwtAccessDeniedHandler;
import com.example.Product_Service.security.JwtAuthenticationEntryPoint;
import com.example.Product_Service.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration for the Product Catalog Service.
 *
 * Design decisions:
 * - Stateless: No server-side sessions — authentication is per-request via JWT cookie.
 * - CSRF disabled: Appropriate for stateless cookie-based APIs (HttpOnly + SameSite=Lax).
 * - No AuthenticationManager/PasswordEncoder: Product-Service does not authenticate users —
 *   that responsibility belongs entirely to Auth-Service.
 * - @EnableMethodSecurity: Enables @PreAuthorize annotations on controller methods for
 *   fine-grained role-based access control (e.g. SELLER-only endpoints).
 * - All /api/** endpoints require authentication; /error is public for Spring error handling.
 *
 * Authorization hierarchy:
 *   - HTTP-level: all /api/** endpoints require authentication (checked here)
 *   - Method-level: @PreAuthorize on controller methods enforces roles
 *     (e.g. @PreAuthorize("hasRole('SELLER')") for create/update/delete)
 *   - Business-level: ProductServiceImpl checks sellerId ownership for update/delete
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Enable CORS with defaults (allows cross-origin requests from frontend)
            .cors(Customizer.withDefaults())

            // Disable CSRF — stateless cookie-based REST API
            .csrf(AbstractHttpConfigurer::disable)

            // Custom handlers for 401 (unauthenticated) and 403 (forbidden) — return JSON
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )

            // Stateless sessions — no HttpSession created or used
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization rules:
            //   /error — public (Spring's built-in error endpoint)
            //   /actuator/** — public (health checks, metrics — add security in production)
            //   Everything else — must be authenticated
            //   Role enforcement is done at method level via @PreAuthorize
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            )

            // Add JWT filter before Spring's username/password authentication filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
