package vut.fit.gja2023.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * A business object representing a project assignment.
 */
@Entity
@Data
@Table(name = "project_assignment")
public class ProjectAssignmentBo {
    public static final int TITLE_MAX_LENGTH = 30;
    public static final int NAME_MAX_LENGTH = 30;
    public static final int DESCRIPTION_MAX_LENGTH = 100;

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = NAME_MAX_LENGTH)
    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Size(max = TITLE_MAX_LENGTH)
    @Column(name = "title", nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Size(max = DESCRIPTION_MAX_LENGTH)
    @Column(name = "description", nullable = false, length = DESCRIPTION_MAX_LENGTH)
    private String description;

    @Column(name = "team", nullable = false)
    private boolean isTeam;

    @Column(name = "deadline", nullable = false)
    private Date deadline;

    @ManyToOne
    private CourseBo course;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.REMOVE)
    private List<ProjectBo> projects;
}
