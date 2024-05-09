package com.maksympanov.hneu.mjt.sbcrud.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksympanov.hneu.mjt.sbcrud.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class CredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final ObjectMapper mapper;

    public CredentialsAuthenticationFilter(String uri, AuthenticationManager authenticationManager) {
        super();
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(uri, "POST"));
        this.authenticationManager = authenticationManager;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            var loginDto = mapper.readValue(req.getInputStream(), LoginDto.class);
            log.info("Login attempt in CredentialsAuthenticationFilter, username: {}", loginDto.getUsername());
            var token = new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword(),
                    new ArrayList<>()
            );
            return authenticationManager.authenticate(token);
        } catch (IOException e) {
            log.warn("Login failure - could not read request data");
        }
        return null;
    }

}
