package com.travelbnb.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.travelbnb.Entity.AppUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {


    @Value("${jwt.algorithm.key}")
    private String algorithmKey;


    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private int expiryTime;  // Duration in milliseconds

    private Algorithm algorithm;

    private static final String USER_NAME = "username";


    @PostConstruct
    public void postConstruct() {

        this.algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateToken(AppUser appUser) {
        return JWT.create()
                .withClaim(USER_NAME, appUser.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryTime))
                .withIssuer(issuer)
                .sign(algorithm);
    }
    public String getUsername(String token){
        DecodedJWT decodeJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        return decodeJWT.getClaim(USER_NAME).asString();

    }


    }
