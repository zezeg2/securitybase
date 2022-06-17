package com.example.securitybase.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute("exception");
        ExceptionCode exceptionCode = null;
        log.debug("log: exception: {} ", exception);

        /**
         * 최초 로그인 실패(아이디 및 비밀번호 불일치)
         */
        if (exception.equals(ExceptionCode.NOT_FOUND_USER.getCode())) {
            exceptionCode = ExceptionCode.NOT_FOUND_USER;
        }
        /**
         * 토큰 없는 경우
         */
        if (exception.equals(ExceptionCode.NONE_TOKEN.getCode())) {
            exceptionCode = ExceptionCode.NONE_TOKEN;
        }

        /**
         * 토큰 만료된 경우
         */
        if (exception.equals(ExceptionCode.EXPIRED_TOKEN.getCode())) {
            exceptionCode = ExceptionCode.EXPIRED_TOKEN;
        }

        /**
         * 토큰 시그니처가 다른 경우
         */
        if (exception.equals(ExceptionCode.INVALID_TOKEN.getCode())) {
            exceptionCode = ExceptionCode.INVALID_TOKEN;
        }

        ExceptionPayload payload = new ExceptionPayload(exceptionCode);
        setResponse(response, payload);
    }

    private void setResponse(HttpServletResponse response, ExceptionPayload payload) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper om = new ObjectMapper();
            om.writeValue(os, payload);
            os.flush();
        }
    }

}
