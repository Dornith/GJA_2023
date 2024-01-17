package vut.fit.gja2023.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A business object representing a team.
 */
@Entity
@Data
@ToString(exclude = "members")
@EqualsAndHashCode(exclude = "members")
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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "team_user",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private List<UserBo> members;

    @ManyToOne
    private CourseBo course;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<ProjectBo> projects;
}
