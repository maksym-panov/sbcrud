package com.maksympanov.hneu.mjt.sbcrud.sbcrud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.AbstractResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.BackendErrorCode;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.MessageResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class JwtProviderService {

    private final ObjectMapper mapper;

    public JwtProviderService() {
        this.mapper = new ObjectMapper();
    }

    public void addHeadersInResponse(HttpServletResponse response, String jwtToken) {
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtToken);
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
    }

    @SneakyThrows
    public void writeTokenInResponse(HttpServletResponse response, String jwtToken) {
        jwtToken = "Bearer " + jwtToken;
        var tokenDto = new TokenDto(jwtToken);
        var rv = new AbstractResponseDto<>(tokenDto);
        mapper.writeValue(response.getOutputStream(), rv);
    }

    @SneakyThrows
    public void writeAuthenticationErrorInResponse(HttpServletResponse response, BackendErrorCode errorCode) {
        var error = MessageResponseDto.failure(errorCode, "Authentication failed");
        var rv = new AbstractResponseDto<>(error);
        mapper.writeValue(response.getOutputStream(), rv);
    }

    public String getAuthorizationNullable(HttpServletRequest request) {
        var header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && (header.startsWith("Bearer "))) {
            return header.substring(7);
        }
        return null;
    }
}
