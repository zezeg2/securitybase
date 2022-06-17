package com.example.securitybase.config;

import com.example.securitybase.config.auth.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityBeans {

    private final DataSource dataSource;

    private final PrincipalDetailsService principalDetailsService;

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_MEMBER > ROLE_GUEST");
        return roleHierarchy;
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답할때 json을 자바스크립트에서 처리할 수 있게 할지를 설정
        config.addAllowedOrigin("*"); // 모든 IP에 응답을 허용
        config.addAllowedHeader("*"); // 모든 Header에 응답을 허용
        config.addAllowedMethod("*"); // 모든 Http Method 요청을  허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
    @Bean
    PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try {
            repository.removeUserTokens("1");
        } catch (Exception ex) {
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices("gymcot",
                        principalDetailsService,
                        tokenRepository()
                );
        service.setTokenValiditySeconds(60*24*30);
        return service;
    }
}
