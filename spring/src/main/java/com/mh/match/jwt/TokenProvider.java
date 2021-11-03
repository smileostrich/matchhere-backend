package com.mh.match.jwt;

import com.mh.match.member.dto.request.TokenDto;

import com.mh.match.member.entity.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 10000 * 60 * 60 * 24 * 7;            // for dev
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 2;            // 2시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserIdFromJwt(String jwt) {
        return parseClaims(jwt).getSubject();
    }

    public ConcurrentHashMap<String, String> getUserDataFromJwt(String jwt) {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap();
        Claims claims = parseClaims(jwt);
        concurrentHashMap.put("userid", claims.getSubject());
//        concurrentHashMap.put("email", claims.get("email").toString());
        concurrentHashMap.put("nickname", claims.get("nickname").toString());
        return concurrentHashMap;
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Object principal = authentication.getPrincipal();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setIssuer("moonilmin")
                .setIssuedAt(new Date(now))
                .setClaims(createClaims(principal))
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();
        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setIssuer("moonilmin")
                .setIssuedAt(new Date(now))
                .setClaims(createClaims(principal))
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    private static Map<String, Object> createClaims(Object principal) {
        Map<String, Object> claims = new HashMap<>();
        CustomUserDetails customUserDetails = (CustomUserDetails)principal;
//        if (principal instanceof CustomUserDetails) {
        claims.put("email", customUserDetails.getEmail());
        claims.put("nickname", customUserDetails.getNickname());
//        claims.put("username", customUserDetails.getUsername());
        return claims;
//        }
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
//        Collection<? extends GrantedAuthority> authorities =
        Collection<GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // UserDetails 객체를 만들어서 Authentication 리턴
        CustomUserDetails principal = CustomUserDetails.builder()
                .id(claims.getSubject())
                .authorities(authorities)
                .email(claims.get("email").toString())
                .nickname(claims.get("nickname").toString())
                .build();
//        CustomUserDetails principal = new CustomUserDetails.builder.build()(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            throw e;
        }
//        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
