package JavaProject.MoneyManagement_BE_SE330.config;

import JavaProject.MoneyManagement_BE_SE330.user.ApplicationUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    public String generateJwtToken(Authentication authentication) {
        ApplicationUser userPrincipal = (ApplicationUser) authentication.getPrincipal();
        return generateJwtTokenForUser(userPrincipal);
    }

    public String generateJwtTokenForUser(ApplicationUser user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .setId(UUID.randomUUID().toString())
                .claim("role", user.getRole().getName())
                .claim("uid", user.getId())
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getJwtId(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }

    public boolean validateJwtToken(String authToken, boolean ignoreExpiration) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(authToken);

            if (!ignoreExpiration && claimsJws.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return claimsJws.getBody().getIssuer().equals(jwtIssuer);
        } catch (JwtException e) {
            logger.error("JWT token validation error: {}", e.getMessage());
        }
        return false;
    }
}