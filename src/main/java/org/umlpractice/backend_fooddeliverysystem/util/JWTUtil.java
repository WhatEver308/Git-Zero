package org.umlpractice.backend_fooddeliverysystem.util;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
//import java.sql.Date;
import java.security.SignatureException;
import java.util.Date;

/**
 * JWTUtil 类说明
 *
 * @author 30367
 * @date 2025/5/28 17:53
 */
@Component
public class JWTUtil implements interfaceJWTUtil {

    // 使用 HMAC-SHA256 算法，密钥需保密（生产环境应从配置读取）
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 86400000; // 24小时（单位：毫秒）

    @Override
    public String generateToken(String userName)
    {
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean verifyToken(String token)
    {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        }
        catch(ExpiredJwtException e) {
            throw new SecurityException("Expired JWT token: " + e.getMessage());
        }
        catch (JwtException | IllegalArgumentException e) {
            throw new SecurityException("Invalid JWT token: " + e.getMessage());
        }

    }

    @Override
    public String parseUserNameFromToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
