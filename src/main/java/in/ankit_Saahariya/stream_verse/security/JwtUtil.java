package in.ankit_Saahariya.stream_verse.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final long JWT_TOKEN_VALIDITY= 30L * 24 * 60 * 60 * 1000;
    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String username,String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
        return doGenerateToken(claims,username);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSigningKey())
                .compact();
    }

    private  <T> T getClaimsFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getRoleFromToken(String token){
        return getClaimsFromToken(token,claims -> claims.get("role",String.class));
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimsFromToken(token,Claims::getExpiration);
    }

    public String getUsernameFromToken(String token){
        return getClaimsFromToken(token,Claims::getSubject);
    }


    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }


    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean isValidateToken(String token){
        try {
            getAllClaimsFromToken(token);
            return  !isTokenExpired(token);
        }catch (Exception ex){
            return false;
        }
    }
}
