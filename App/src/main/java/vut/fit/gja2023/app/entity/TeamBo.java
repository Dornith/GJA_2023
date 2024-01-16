package vut.fit.gja2023.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * A business object representing a team.
 */
@Entity
@Data
@Table(name = "team")
public class TeamBo {
    public static final int NAME_MAX_LENGTH = 30;
    public static final int GROUP_NAME_MAX_LENGTH = 30;

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = NAME_MAX_LENGTH)
    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    // linux group identifier
    @Size(max = GROUP_NAME_MAX_LENGTH)
    @Column(name = "group_name", nullable = false, length = GROUP_NAME_MAX_LENGTH)
    private String groupName;

    @ManyToMany(mappedBy = "teams")
    private List<UserBo> members;

    @ManyToOne
    private CourseBo course;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<ProjectBo> projects;
}
