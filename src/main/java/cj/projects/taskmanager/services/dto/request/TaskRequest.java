package cj.projects.taskmanager.services.dto.request;

public record TaskRequest(
        String title,
        String description,
        String status
) {}
