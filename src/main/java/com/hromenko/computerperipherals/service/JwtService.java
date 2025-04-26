package com.hromenko.computerperipherals.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService
{
    @Value("${jwt.secret}")
    private CharSequence secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(UserDetails userDetails){
        Date issuedData=new Date();
        Date expiredData=new Date(issuedData.getTime() +jwtLifeTime.toMillis());
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issuedData)
                .expiration(expiredData)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact()
                ;
    }

    public String getUsername(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean checkToken(String token) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
