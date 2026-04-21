package cj.projects.taskmanager.persistence.entities;

import cj.projects.taskmanager.persistence.entities.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "task")
@Entity
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private LocalDateTime createAd;
    private LocalDateTime updateAd;
    private LocalDateTime deleteAd;

    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

}
