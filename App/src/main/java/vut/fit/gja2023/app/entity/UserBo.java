package vut.fit.gja2023.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vut.fit.gja2023.app.enums.UserRole;

import java.util.List;

/**
 * A business object representing a user.
 */
@Entity
@Data
@Table(name = "user", indexes = {
    @Index(name = "idx_login", columnList = "login")
})
public class UserBo {
    public static final int NAME_MAX_LENGTH = 30;
    public static final String LOGIN_REGEX = "^x[a-z]{5}\\d{2}$";

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = NAME_MAX_LENGTH)
    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Pattern(regexp = LOGIN_REGEX, message = "Invalid login format")
    @Column(name = "login", nullable = false, length = 8)
    private String login;

    @Column(name = "role", nullable = false)
    private UserRole role;

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
    private List<CourseBo> studiedCourses;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<ProjectBo> projects;

    @OneToMany(mappedBy = "guarantor", cascade = CascadeType.REMOVE)
    private List<CourseBo> guaranteedCourses;

    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.REMOVE)
    private List<CourseBo> coordinatedCourses;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<FirewallRuleBo> firewallRules;
}
