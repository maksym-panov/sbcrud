package com.maksympanov.hneu.mjt.sbcrud.sbcrud.filter;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth.CustomAuthenticationToken;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.JwtProviderService;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jodd.util.StringUtil;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtFilter extends BasicAuthenticationFilter {

    private final JwtProviderService jwtProviderService;

    private final JwtService jwtService;

    public JwtFilter(
            AuthenticationManager authenticationManager,
            JwtProviderService jwtProviderService,
            JwtService jwtService
    ) {
        super(authenticationManager);
        this.jwtProviderService = jwtProviderService;
        this.jwtService = jwtService;
    }

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) {

        var authToken = jwtProviderService.getAuthorizationNullable(req);

        if (StringUtil.isBlank(authToken)) {
            chain.doFilter(req, res);
            return;
        }

        var subject = jwtService.getJwtUserSubject(authToken);
        var authentication = new CustomAuthenticationToken(subject);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
    }
}
