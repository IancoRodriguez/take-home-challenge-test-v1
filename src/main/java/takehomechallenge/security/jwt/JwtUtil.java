package takehomechallenge.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // generate secret key to sing tokens
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJwtToken(String email){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

//    public String getEmailFromToken(String token) {
//        return calims(token).getSubject();
//    }

    public String getEmailFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith( getSigningKey() )
                .build()
                .parseSignedClaims(token)
                .getPayload();


        return claims.getSubject();
    }

    public String validateJwtToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true; // Si no lanza excepción, es válido

        } catch (ExpiredJwtException e) {
            System.err.println("Token expirado");
        } catch (JwtException e) {
            System.err.println("Token inválido: " + e.getMessage());
        }
        return false;
    }



}
