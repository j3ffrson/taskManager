package cj.projects.taskmanager.configurations;

import cj.projects.taskmanager.services.dto.exceptions.ErrorExceptionDto;
import cj.projects.taskmanager.util.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenValidator extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final AuthEntryPoint authEntryPoint;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
                String username = jwtUtil.getUsernameFromToken(decodedJWT);
                String authorities = jwtUtil.getSpecificClaim(decodedJWT, "permissions").asString();

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            filterChain.doFilter(request, response);

        } catch (JWTVerificationException e) {
            ErrorExceptionDto dto = new ErrorExceptionDto(401,"Unauthorized", "Invalid or expired JWT token");
            request.setAttribute("error_dto", dto);

            authEntryPoint.commence(request, response,
                    new AuthenticationServiceException("Invalid JWT token", e));
        }
    }

}
