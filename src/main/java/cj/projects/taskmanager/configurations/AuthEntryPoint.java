package cj.projects.taskmanager.configurations;

import cj.projects.taskmanager.services.dto.exceptions.ErrorExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorExceptionDto dto = (ErrorExceptionDto) request.getAttribute("error_dto");


        if (dto == null) {
            // Si viene del UserDetailsService u otro flujo estándar
            String message = authException.getMessage();

            if (authException.getCause() instanceof UsernameNotFoundException) {
                dto = new ErrorExceptionDto(401, "Unauthorized", "User not found");
            } else if (message.contains("Bad credentials")) {
                dto = new ErrorExceptionDto(401, "Unauthorized", "Invalid username or password");
            } else {
                dto = new ErrorExceptionDto(401, "Unauthorized", "Authentication failed");
            }
        }

        new ObjectMapper().writeValue(response.getOutputStream(), dto);
    }
}
