package kr.composite.api.authentication.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import kr.composite.api.authentication.domain.CredentialCodec;
import kr.composite.api.authentication.domain.CredentialPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtCredentialCodec implements CredentialCodec {

    private final SecretKey secretKey;
    private final Duration expiration;

    public JwtCredentialCodec(
            @Value("${authentication.jwt.secret}") String secret,
            @Value("${authentication.jwt.expiration}") Duration expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    @Override
    public String encode(CredentialPayload payload) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration.toMillis());

        return Jwts.builder()
                .subject(String.valueOf(payload.identifier()))
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public CredentialPayload decode(String credential) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(credential)
                    .getPayload();

            return new CredentialPayload(Long.parseLong(claims.getSubject()));
        } catch (ExpiredJwtException e) {
            throw AuthenticationInfrastructureException.expiredCredential(e);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw AuthenticationInfrastructureException.invalidCredential(e);
        } catch (Exception e) {
            throw AuthenticationInfrastructureException.unknown(e);
        }
    }
}
