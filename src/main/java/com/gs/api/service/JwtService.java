package com.gs.api.service;

import com.gs.api.config.RsaKeyProperties;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RsaKeyProperties rsaKeys;
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(UserDetails userDetails) {
        try {
            Instant now = Instant.now();

            String scope = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .issuer("gs-api")
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusMillis(EXPIRATION_TIME)))
                    .claim("scope", scope)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).build(),
                    claimsSet
            );

            signedJWT.sign(new RSASSASigner(rsaKeys.getPrivateKey()));

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting username", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            RSASSAVerifier verifier = new RSASSAVerifier(rsaKeys.getPublicKey());
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            String username = signedJWT.getJWTClaimsSet().getSubject();
            if (!username.equals(userDetails.getUsername())) {
                return false;
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
