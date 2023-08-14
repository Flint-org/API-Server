package com.flint.flint.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 엑세스, 리프레쉬 토큰 생성, 클레임 추출, 토큰 검증
 * @Author 정순원
 * @Since 2023-08-14
 */
@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.SecretKey}")
    private String secretKey;
    @Value("${jwt.accessTokenExpiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refreshTokenExpiration}")
    private Long refreshTokenExpiration;

    //토큰 생성 메소드들
    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(userDetails, new HashMap<>());
    }

    public String generateAccessToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return buildToken(extraClaims, userDetails, accessTokenExpiration);
    }


    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    private String buildToken(Map<String, Object> extraclaims,
                              UserDetails userDetails,
                              Long expiration) {
        return Jwts.
                builder()
                .setClaims(extraclaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰에서 클레임추출 메소드들
    public String parseMemberName(String token) {
        return parseAllClaims(token).getSubject();
    }

    public Date parseExpiration(String token) {
        return parseAllClaims(token).getExpiration();
    }

    private Claims parseAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //토큰 검증 메소드
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = parseMemberName(token);
        final Date expiration = parseExpiration(token);
        return (username.equals(userDetails.getUsername())) && expiration.before(new Date());
    }
}
