package com.example.observationcalendar.security;

import com.example.observationcalendar.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    private SecretKey getSigningKey() {
        String configKey = jwtConfig.getSecretKey();
        byte[] keyBytes = configKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + jwtConfig.getExpirationMs());

        JwtBuilder builder = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        return builder.compact();
    }

    public String generateRefreshToken(String username) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtConfig.getRefreshExpirationMs());

        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("type", "refresh")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        return builder.compact();
    }

    public String getUsernameFromToken(String token) {
        JwtParserBuilder parserBuilder = Jwts.parser()
                .setSigningKey(getSigningKey());
        JwtParser parser = parserBuilder.build();
        Jws<Claims> claimsJws = parser.parseClaimsJws(token);
        Claims claims = claimsJws.getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            JwtParserBuilder parserBuilder = Jwts.parser()
                    .setSigningKey(getSigningKey());
            JwtParser parser = parserBuilder.build();
            parser.parseClaimsJws(authToken);
            return true;
        } catch(SecurityException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch(MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch(ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch(UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch(IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    public Date getExpirationDateFromToken(String token) {
        JwtParserBuilder parserBuilder = Jwts.parser()
                .setSigningKey(getSigningKey());
        JwtParser parser = parserBuilder.build();
        Jws<Claims> claimsJwts = parser.parseClaimsJws(token);
        Claims claims = claimsJwts.getBody();

        return claims.getExpiration();
    }
}