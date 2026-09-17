package com.trimzo.config;

import com.trimzo.entity.ApiKey;
import com.trimzo.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Check if X-API-Key header is present
        String apiKey = request.getHeader("X-API-Key");

        // If no API key header — skip this filter
        if (apiKey == null || apiKey.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // If user already authenticated via JWT — skip
        if (SecurityContextHolder.getContext()
                .getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Find matching active key using BCrypt
        ApiKey matchedKey = findMatchingKey(apiKey);

        if (matchedKey == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API key");
            return;
        }

        // Update last used timestamp
        matchedKey.setLastUsed(LocalDateTime.now());
        apiKeyRepository.save(matchedKey);

        // Set authentication in security context
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        User.withUsername(
                                        matchedKey.getUser().getEmail())
                                .password("")
                                .authorities(List.of(
                                        new SimpleGrantedAuthority("ROLE_USER")))
                                .build(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        auth.setDetails(new WebAuthenticationDetailsSource()
                .buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    /**
     * Finds an active API key that matches the provided raw key.
     * Uses BCrypt matching since hashes cannot be reversed.
     */
    private ApiKey findMatchingKey(String rawKey) {
        return apiKeyRepository.findAllActiveWithUser()
                .stream()
                .filter(key -> passwordEncoder
                        .matches(rawKey, key.getKeyValue()))
                .findFirst()
                .orElse(null);
    }
}