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

    @ManyToOne
    private UserBo guarantor;

    @ManyToOne
    private UserBo coordinator;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<TeamBo> teams;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private List<ProjectAssignmentBo> projectAssignments;

    @ManyToMany(mappedBy = "studiedCourses")
    private List<UserBo> students;
}
