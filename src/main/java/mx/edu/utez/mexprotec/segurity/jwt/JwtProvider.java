package mx.edu.utez.mexprotec.segurity.jwt;


import io.jsonwebtoken.*;
import mx.edu.utez.mexprotec.segurity.model.AuthUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
    private  final static Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication) {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        String tokenId = UUID.randomUUID().toString();

        Date issuedAt = new Date();

        Date expirationDate = new Date(issuedAt.getTime() + expiration * 1000L);

        return Jwts.builder()
                .setId(tokenId)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            LOGGER.error("Token mal formado");
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Token no soportado");
        } catch (ExpiredJwtException e) {
            LOGGER.error("Token caducado");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Token no provisto");
        } catch (SignatureException e) {
            LOGGER.error("Error en la firma del token");
        } catch (Exception e) {
            LOGGER.error("Error en la firma del token");
        }
        return false;
    }

    public String generatePasswordResetToken(String email) {
        return Jwts.builder().setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                new Date().getTime() + expiration * 1000L
                        )
                )
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String getEmailFromPasswordResetToken(String token) {
        try {
            if (validateToken(token)) {
                return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("Error en la firma del token");
            return null;
        }

    }
}
