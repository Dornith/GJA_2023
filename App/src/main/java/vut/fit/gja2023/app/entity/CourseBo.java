package vut.fit.gja2023.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "course")
public class CourseBo {
    public static final int NAME_MAX_LENGTH = 30;

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = NAME_MAX_LENGTH)
    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @OneToMany(mappedBy = "course")
    private List<TeamBo> teams;

    @OneToMany(mappedBy = "course")
    private List<ProjectAssignmentBo> projectAssignments;

    @ManyToMany(mappedBy = "courses")
    private List<UserBo> users;
}
