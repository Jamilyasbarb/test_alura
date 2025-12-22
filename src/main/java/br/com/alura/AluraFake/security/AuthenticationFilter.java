package br.com.alura.AluraFake.security;

import br.com.alura.AluraFake.dto.auth.AuthLoginDTO;
import br.com.alura.AluraFake.exception.DataIntegrityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthenticationFailureHandler failureHandler;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, AuthenticationFailureHandler failureHandler) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            AuthLoginDTO credentials = new ObjectMapper().readValue(request.getInputStream(), AuthLoginDTO.class);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new DataIntegrityException("Erro ao ler credenciais de login", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication auth) throws IOException {
        var user = (UserDetailsImpl) auth.getPrincipal();
        String token = jwtService.generateToken(user.getUsername(), user.getId(), user.getAuthorities());

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("RoleName", user.getRole().name());
        response.getWriter().write("{" +
                "\"token\": \"" + "Bearer " + token + "\",\n" +
                "\"role\": \"" + user.getRole().name() + "\"" +
                "}");
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
