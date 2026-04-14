package cj.projects.taskmanager.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "author")
@Entity
public class PermissionEntity {

    @Id
    @GeneratedValue
    private UUID uuid;
    @Column(nullable = false,updatable = false)
    private String name;
}
