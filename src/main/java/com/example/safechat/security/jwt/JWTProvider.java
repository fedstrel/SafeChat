package com.example.safechat.security.jwt;

import com.example.safechat.entity.User;
import com.example.safechat.security.SecurityConstants;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTProvider.class);

    public String generateToken(Authentication authentication){
        User user = (User)authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expirationTime = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String usersId = Long.toString(user.getId());
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", usersId);
        claimsMap.put("username", user.getUsername());
        claimsMap.put("firstname", user.getFirstname());
        claimsMap.put("lastname", user.getLastname());

        return Jwts.builder()
                .setSubject(usersId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY)
                .compact();
    }

    public boolean validToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException |
                 MalformedJwtException |
                 SignatureException |
                 UnsupportedJwtException |
                 IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return false;
        }
    }

    public Long getUsersIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        String usersId = (String) claims.get("id");
        return Long.parseLong(usersId);
    }
}
