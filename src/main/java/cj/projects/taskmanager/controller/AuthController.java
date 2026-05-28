package cj.projects.taskmanager.controller;

import cj.projects.taskmanager.services.dto.request.AuthCreateUserRequest;
import cj.projects.taskmanager.services.dto.request.AuthLoginRequest;
import cj.projects.taskmanager.services.dto.response.AuthResponse;
import cj.projects.taskmanager.services.implementation.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("permitAll()")
@RequiredArgsConstructor
@RequestMapping(value = "/api/tasks",version = "1.0")
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest authLoginRequest){
        return new ResponseEntity<>(userDetailsService.loginUser(authLoginRequest), HttpStatus.OK);
    }

    @PostMapping("/sign")
    public ResponseEntity<AuthResponse> sign(@Valid @RequestBody AuthCreateUserRequest authCreateUserRequest){
        return new ResponseEntity<>(userDetailsService.createUser(authCreateUserRequest), HttpStatus.CREATED);
    }

}
