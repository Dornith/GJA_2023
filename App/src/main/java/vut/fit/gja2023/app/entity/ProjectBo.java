package vut.fit.gja2023.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "project")
public class ProjectBo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "submitted", nullable = true)
    private Date submitted;

    @ManyToOne
    private ProjectAssignmentBo assignment;

    @ManyToOne
    private TeamBo team;

    public boolean isSubmitted() {
        return submitted != null;
    }
}
