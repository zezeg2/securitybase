package com.example.securitybase.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.securitybase.config.auth.PrincipalDetails;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;


public interface JwtProperties {

    String SECRET = "jby";
    //    Integer EXPIRATION_TIME = 1000 * 60 * 30;
//    Integer EXPIRATION_TIME = 60*24*1000;
    Integer EXPIRATION_TIME = 30*1000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    static String genToken(HttpServletResponse response, PrincipalDetails principalDetail) throws UnsupportedEncodingException {
        String jwtToken = JWT.create()
                .withSubject(principalDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("id", principalDetail.getUser().getId())
                .withClaim("username", principalDetail.getUser().getUsername())
                .sign(Algorithm.HMAC512(SECRET));
        String jwt = URLEncoder.encode(TOKEN_PREFIX + jwtToken, "UTF-8");
        response.addCookie(new Cookie("Authorization", jwt));
        return jwt;
    }
}

