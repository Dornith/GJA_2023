package vut.fit.gja2023.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vut.fit.gja2023.app.enums.Role;

import java.util.List;

@Entity
@Data
@Table(name = "user")
public class UserBo {
    public static final int NAME_MAX_LENGTH = 30;

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = NAME_MAX_LENGTH)
    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Column(name = "role", nullable = false)
    private Role role;

    @ManyToMany
    @JoinTable(
        name = "user_team",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id")
    )
    private List<TeamBo> teams;

    @ManyToMany
    @JoinTable(
        name = "user_course",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private List<CourseBo> courses;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<FirewallRuleBo> firewallRules;
}
