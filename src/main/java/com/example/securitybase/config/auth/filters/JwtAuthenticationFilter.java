package com.example.securitybase.config.auth.filters;


import com.example.securitybase.config.auth.PrincipalDetails;
import com.example.securitybase.domain.user.User;
import com.example.securitybase.error.CustomAuthenticationFailureHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.securitybase.config.JwtProperties.genToken;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private RememberMeServices rememberMeServices;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        super(authenticationManager);
        this.rememberMeServices = rememberMeServices;
    }

    @Override
    public RememberMeServices getRememberMeServices() {
        return super.getRememberMeServices();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user;
            ObjectMapper om = new ObjectMapper();
            user = om.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            Authentication authentication = super.getAuthenticationManager().authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("Login successful : {}", principalDetails.getUser().getUsername());
            return authentication;

        }
        catch (InternalAuthenticationServiceException | BadCredentialsException e) {
            setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

            try {
                unsuccessfulAuthentication(request, response, e);
            }
            catch (IOException | ServletException ex) {
                ex.printStackTrace();
            }

        }

        catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Login Request Denied");
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain
            chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetail = (PrincipalDetails) authResult.getPrincipal();

        /* Hash 암호방식 */
        String jwt = genToken(response, principalDetail);
        log.info("Authentication Header : {}", jwt);
        super.setRememberMeServices(rememberMeServices);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}