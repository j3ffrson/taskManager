package cj.projects.taskmanager.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "author")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue
    private UUID uuid;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;

    @Column(unique = true)
    private String username;

    private boolean isEnabled;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<TaskEntity> tasks= new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();


}
