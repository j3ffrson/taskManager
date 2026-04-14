package cj.projects.taskmanager.persistence.entities;

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
    @GeneratedValue
    private UUID uuid;

    private String title;
    private String description;
    private LocalDateTime createAd;
    private LocalDateTime updateAd;
    private LocalDateTime deleteAd;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

}
