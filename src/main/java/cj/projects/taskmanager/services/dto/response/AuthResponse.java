package cj.projects.taskmanager.services.dto.response;

public record AuthResponse(
    String username,
    String[] roles,
    String JWT,
    boolean status
) {}
