package com.example.securitybase.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String ex = (String) request.getAttribute("exception");
        log.debug("log: exception: {} ", exception);
        ExceptionCode exceptionCode = null;

        if (ex.equals(ExceptionCode.NOT_FOUND_USER.getCode())) {
            exceptionCode = ExceptionCode.NOT_FOUND_USER;
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
