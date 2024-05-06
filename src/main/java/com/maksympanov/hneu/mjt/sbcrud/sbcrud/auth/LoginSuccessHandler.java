package com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.JwtProviderService;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Slf4j
@AllArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProviderService jwtProviderService;

    private final JwtService jwtService;

    @Override
    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var auth = AuthContextHolder.getAuthentication();
        var subject = auth.getSubject();

        var jwtToken = jwtService.getJwtFromSubject(subject);

        log.info("Result JWT-token - {}", jwtToken);

        jwtProviderService.addHeadersInResponse(response, jwtToken);
        jwtProviderService.writeTokenInResponse(response, jwtToken);

        clearAuthenticationAttributes(request);

        log.debug("Successful authentication");
    }

}
