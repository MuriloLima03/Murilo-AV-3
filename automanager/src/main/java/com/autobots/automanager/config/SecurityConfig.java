package com.autobots.automanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (required for H2 console + APIs in dev)
                .csrf(AbstractHttpConfigurer::disable)

                // Allow H2 console without authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers().permitAll()
                        .anyRequest().authenticated()
                )

                // Required so H2 console can render inside iframe
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Keep HTTP Basic auth for your API
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}