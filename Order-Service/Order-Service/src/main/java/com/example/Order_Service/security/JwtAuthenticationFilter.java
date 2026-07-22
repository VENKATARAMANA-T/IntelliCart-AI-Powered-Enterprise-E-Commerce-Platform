package com.example.Order_Service.security;
import com.example.Order_Service.dto.response.AuthValidationResponse;
import com.example.Order_Service.feign.AuthServiceClient;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Autowired
    private AuthServiceClient authServiceClient;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromCookie(request, "access_token");
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                AuthValidationResponse validationResponse = authServiceClient.validateToken("Bearer " + token);
                if (validationResponse != null && validationResponse.getUserId() != null && validationResponse.getUsername() != null && validationResponse.getRole() != null) {
                    AuthenticatedUser authenticatedUser = new AuthenticatedUser(validationResponse.getUserId(), validationResponse.getUsername(), validationResponse.getRole());
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + validationResponse.getRole()));
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authenticated user: {} [{}] for request: {}", validationResponse.getUsername(), validationResponse.getRole(), request.getRequestURI());
                }
            } catch (FeignException.Unauthorized | FeignException.Forbidden e) {
                log.warn("Token validation rejected by Auth-Service for {}: {}", request.getRequestURI(), e.getMessage());
            } catch (FeignException e) {
                log.error("Auth-Service error during token validation for {}: {} [status={}]", request.getRequestURI(), e.getMessage(), e.status());
            } catch (Exception e) {
                log.error("Unexpected error during token validation for {}: {}", request.getRequestURI(), e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) { for (Cookie cookie : cookies) { if (cookieName.equals(cookie.getName())) { return cookie.getValue(); } } }
        return null;
    }
}
