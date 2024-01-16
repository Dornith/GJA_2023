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

    @Column(name = "submitted_date", nullable = false)
    private Date submittedDate;

    @Column(name = "submitted", nullable = false)
    private boolean isSubmitted;

    @ManyToOne
    private ProjectAssignmentBo assignment;

    @ManyToOne
    private TeamBo team;

    @ManyToOne
    private UserBo author;
}
