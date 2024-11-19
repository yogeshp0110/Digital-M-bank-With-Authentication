package com.maveric.bank.JWT;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    Claims claims = null;
    private String userName = null;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = httpServletRequest.getServletPath();
        System.out.println("Processing request: " + requestPath);

        // Allow public endpoints without authentication
        if (requestPath.matches("/user/login|/user/signup|/user/forgotPassword")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            userName = jwtUtils.extractUsername(token);
            claims = jwtUtils.extractAllClaims(token);

            System.out.println("Extracted Token: " + token);
            System.out.println("User from Token: " + userName);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customerUserDetailsService.loadUserByUsername(userName);

            if (jwtUtils.validateToken(token, userDetails)) {
                List<GrantedAuthority> authorities = extractRolesFromToken(claims);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("Invalid token for user: " + userName);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    /**
     * Extracts roles from the JWT claims and converts them to GrantedAuthority objects.
     */
    private List<GrantedAuthority> extractRolesFromToken(Claims claims) {
        // Extract 'role' from claims instead of 'roles'
        List<String> roles = claims.get("role", List.class);

        if (roles == null) {
            System.out.println("No roles found in the token.");
            return List.of();  // Return empty list if roles are missing
        }

        return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
    }


    public boolean hasRole(String role) {
        return claims != null && ((List<String>) claims.get("roles")).contains(role);
    }

    public boolean isSuperAdmin() {
        return hasRole("SUPERADMIN");
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public boolean isUser() {
        return hasRole("CUSTOMER");
    }

    public String getCurrentUser() {
        return userName;
    }
}
