package com.devotee.resume.init.security;

import com.devotee.resume.init.Repository.UserRepository;
import com.devotee.resume.init.document.User;
import com.devotee.resume.init.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("Inside JwtAuthenticationFilter");

        String authHeader =
                request.getHeader("Authorization");

        String token = null;
        String userId = null;

        // EXTRACT TOKEN
        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            try {

                userId =
                        jwtUtil.getUserIdFromToken(token);

                log.info(
                        "User ID extracted from token : {}",
                        userId
                );

            } catch (Exception e) {

                log.error(
                        "Invalid JWT token : {}",
                        e.getMessage()
                );
            }
        }

        // VALIDATE + AUTHENTICATE
        if (userId != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            try {

                if (jwtUtil.validateToken(token) &&
                        !jwtUtil.isTokenExpired(token)) {

                    User user =
                            userRepository
                                    .findById(userId)
                                    .orElseThrow(() ->
                                            new UsernameNotFoundException(
                                                    "User not found"
                                            ));

                    UsernamePasswordAuthenticationToken
                            authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    new ArrayList<>()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);

                    log.info(
                            "User authenticated successfully"
                    );
                }

            } catch (Exception e) {

                log.error(
                        "Exception while validating token : {}",
                        e.getMessage()
                );
            }
        }

        filterChain.doFilter(request, response);
    }
}