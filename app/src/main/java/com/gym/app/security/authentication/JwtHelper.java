package com.gym.app.security.authentication;

import com.gym.app.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtHelper {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final long ALLOWED_CLOCK_SKEW = 5 * 60 * 1000; // 5 minutes
    private static final long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date(System.currentTimeMillis() - ALLOWED_CLOCK_SKEW));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        Set<Role> userRoles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::getRoleFromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        boolean isAuthenticated = hasAnyRole(userRoles, Arrays.asList(Role.ADMIN, Role.TRAINER, Role.ATHLETE));

        return isAuthenticated && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean hasAnyRole(Set<Role> userRoles, List<Role> requiredRoles) {
        return userRoles.stream().anyMatch(requiredRoles::contains);
    }

    private boolean hasRole(Collection<? extends GrantedAuthority> authorities, Role role) {
        String roleName = "ROLE_" + role.name();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    public String generateToken(String userName, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME)) // 1 hour expiration
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Role getRoleFromString(String roleString) {
        try {
            if(roleString.startsWith("ROLE_")) {
                return Role.valueOf(roleString.substring("ROLE_".length()));
            }
            return Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            System.err.println("Unknown role: " + roleString);
            return null; // Return null for unknown roles
        }
    }
}