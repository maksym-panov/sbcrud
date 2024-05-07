package com.maksympanov.hneu.mjt.sbcrud.sbcrud.config;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.filter.CredentialsAuthenticationFilter;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth.CredentialsAuthenticationProvider;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth.LoginSuccessHandler;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.filter.JwtFilter;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.JwtProviderService;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.UserRole.*;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private CredentialsAuthenticationProvider credentialsAuthenticationProvider;

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) {
        return http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(ar ->
                        ar.requestMatchers(APIPrefixes.USER + "/**").hasAnyRole(USER.name())
                                .requestMatchers(APIPrefixes.VENDOR + "/**").hasAnyRole(VENDOR.name())
                                .requestMatchers(APIPrefixes.ADMIN + "/**").hasAnyRole(ADMIN.name())
                                .requestMatchers(
                                        APIPrefixes.PUBLIC + "/**",
                                        "/docs/**",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                .anyRequest()
                                .authenticated()
                        )
                        .addFilter(jwtFilter(authenticationManager))
                        .addFilter(credentialsAuthFilter(authenticationManager))
                        .build();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(HttpSecurity http) {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return builder
                .authenticationProvider(credentialsAuthenticationProvider)
                .build();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtProviderService(), jwtService());
    }

    public CredentialsAuthenticationFilter credentialsAuthFilter(AuthenticationManager authenticationManager) {
        var filter = new CredentialsAuthenticationFilter(APIPrefixes.PUBLIC + "/login", authenticationManager);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
        return filter;
    }

    public JwtFilter jwtFilter(AuthenticationManager authenticationManager) {
        return new JwtFilter(authenticationManager, jwtProviderService(), jwtService());
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public JwtProviderService jwtProviderService() {
        return new JwtProviderService();
    }

}
