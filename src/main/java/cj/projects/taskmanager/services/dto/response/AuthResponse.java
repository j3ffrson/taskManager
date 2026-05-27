package cj.projects.taskmanager.services.dto.response;

import java.util.List;

public record AuthResponse(
    String username,
    List<String> roles,
    String JWT,
    boolean status
) {}
