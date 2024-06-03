package com.airbnb.service;

import com.airbnb.entity.PropertyUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

   @Value("${jwt.secretKey}")
   private String secretKey;
   @Value("${jwt.issuer}")
   private String issuer;
   @Value("${jwt.expiryTime}")
   private int expiryTime; //Expiry Time will calculate in milliseconds -> 1 min = 60sec * 1000 milliseconds
   // so calculate 1 hour -> 60min * 60sec * 1000 milli sec.

   private Algorithm algorithm;

   private final static String USER_NAME = "username";

   @PostConstruct//it will run only once automatically
   public void postConstruct(){
      algorithm = Algorithm.HMAC256(secretKey);
   }

   public String generateTocken(PropertyUser propertyUser) {
     return JWT.create()
              .withClaim(USER_NAME, propertyUser.getUsername())
              .withIssuer(issuer)
              .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime))
              .sign(algorithm);
   }

   public String getUsername(String token){
      DecodedJWT decodeJWT = JWT.require(algorithm)
              .withIssuer(issuer).build().verify(token);
      return decodeJWT.getClaim(USER_NAME).asString();
   }
}