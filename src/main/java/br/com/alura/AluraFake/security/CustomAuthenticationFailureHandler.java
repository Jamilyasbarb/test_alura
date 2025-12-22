package br.com.alura.AluraFake.security;

import br.com.alura.AluraFake.exception.AuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errorMessage = "Falha na autenticação";

        if (exception.getCause() instanceof AuthorizationException) {
            errorMessage = exception.getCause().getMessage();
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Credenciais inválidas";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().append(json(errorMessage));
    }

    private String json(String msg) {
        String error = "Nome de usuário ou senha inválidos";
        if (msg != null && !msg.isEmpty()) {
            if (msg.equals("Credenciais inválidas")) {
                msg = "Login ou senha incorretos";
            }
            error = msg;
        }
        long date = new Date().getTime();
        return "{\"timestamp\": " + date + ", " + "\"status\": 401, " + "\"error\": \"Erro de autenticação\", "
                + "\"message\": \"" + error + "\", " + "\"path\": \"/login\"}";
    }
}
