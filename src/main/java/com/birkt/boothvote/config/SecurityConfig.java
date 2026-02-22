package com.birkt.boothvote.config;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;

@Component
class Config {

}
@Configuration
public class SecurityConfig {


    // TEMPORARY: Security is disabled for local testing only
    // ⚠️ Remove before production deployment
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())           // disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()           // åpner alle endpoints
                );

        return http.build();
    }
}