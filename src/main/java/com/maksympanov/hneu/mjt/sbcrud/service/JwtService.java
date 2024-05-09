package com.maksympanov.hneu.mjt.sbcrud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksympanov.hneu.mjt.sbcrud.auth.AuthenticatedUser;
import com.maksympanov.hneu.mjt.sbcrud.auth.AuthenticatedUserSerializable;
import com.maksympanov.hneu.mjt.sbcrud.exception.AuthException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.SneakyThrows;
import org.springframework.security.authentication.BadCredentialsException;

import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtService {

    private final JWSSigner jwsSigner;

    private final JWSVerifier jwsVerifier;

    private final ObjectMapper mapper;

    @SneakyThrows
    public JwtService() {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        var keyPair = generator.generateKeyPair();
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = (RSAPrivateKey) keyPair.getPrivate();

        System.out.println("RSA Public Key: " + publicKey);
        System.out.println("RSA Private Key: " + privateKey);

        this.jwsSigner = new RSASSASigner(privateKey);
        this.jwsVerifier = new RSASSAVerifier(publicKey);
        this.mapper = new ObjectMapper();
    }

    public String getJwtFromSubject(AuthenticatedUser subject) {
        var expiration = 3600000L;

        var serializable = subject.toSerializable();
        var jwtPayload = createPayload(serializable, expiration, new HashMap<>());
        var jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.PS512), jwtPayload);

        try {
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public AuthenticatedUser getJwtUserSubject(String token) {
        var claims = parseToken(token);
        var userClaim = claims.getSubject();
        if (userClaim == null) {
            throw new BadCredentialsException("There is no user claim in JWT token");
        }
        var serializable = mapper.readValue(userClaim, AuthenticatedUserSerializable.class);
        return serializable.toAuthenticatedUser();
    }

    // Private

    @SneakyThrows
    private Payload createPayload(AuthenticatedUserSerializable subject, Long expiration, Map<String, Object> claims) {

        var builder = new JWTClaimsSet.Builder();

        builder
                .audience("sbcrud_audience")
                .subject(mapper.writeValueAsString(subject))
                .expirationTime(new Date(System.currentTimeMillis() + expiration))
                .issueTime(new Date())
                .issuer("maksympanov.com")
                .claim("issued_at", System.currentTimeMillis());

        claims.forEach(builder::claim);

        var resultClaims = builder.build();

        return new Payload(resultClaims.toJSONObject());
    }

    private JWTClaimsSet parseToken(String token) {
        var claims = parseTokenHelper(token);
        if (!(claims.getAudience().contains("audience"))) {
            throw new AuthException("Wrong audience in JWT");
        }

        long expirationTime = claims.getExpirationTime().getTime();
        long currentTime = System.currentTimeMillis();


        if (!(expirationTime >= currentTime)) {
            throw new AuthException("Token is expired");
        }

        return claims;
    }


    private JWTClaimsSet parseTokenHelper(String token) {
        try {
            var jwsObject = JWSObject.parse(token);
            if (jwsObject.verify(jwsVerifier)) {
                return JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());
            }
        } catch (JOSEException | ParseException e) {
            throw new AuthException(e.getMessage());
        }

        throw new AuthException("Unknown token owner");
    }

}
