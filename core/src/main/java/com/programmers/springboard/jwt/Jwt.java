package com.programmers.springboard.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Jwt {
    private final String issuer;
    private final String clientSecret;
    private final int expirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int expirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.expirySeconds = expirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    public String sign(Claims claims){
        Date date = new Date();
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(date);
        if(expirySeconds > 0){
            builder.withExpiresAt(new Date(date.getTime() + expirySeconds*1000L));
        }
        builder.withClaim("username", claims.username);
        builder.withArrayClaim("roles", claims.roles);
        return builder.sign(algorithm);
    }

    public Claims verify(String token){
        return new Claims(jwtVerifier.verify(token));
    }

    @Getter
    static public class Claims {
        private String username;
        private String[] roles;
        private Date iat;
        private Date exp;

        private Claims(){}

        Claims(DecodedJWT decodedJWT){
            Claim username = decodedJWT.getClaim("username");
            if(!username.isNull()) {
                this.username = username.asString();
            }
            Claim roles = decodedJWT.getClaim("roles");
            if(!username.isNull()) {
                this.roles = roles.asArray(String.class);
            }
            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims from(String username, String[] roles){
            Claims claims = new Claims();
            claims.username = username;
            claims.roles = roles;
            return claims;
        }

        public Map<String, Object> asMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("username", username);
            map.put("roles", roles);
            map.put("iat", iat());
            map.put("exp", exp());
            return map;
        }

        public long iat(){
            return iat != null ? iat.getTime() : -1;
        }

        public long exp(){
            return exp != null ? exp.getTime() : -1;
        }
    }



}
