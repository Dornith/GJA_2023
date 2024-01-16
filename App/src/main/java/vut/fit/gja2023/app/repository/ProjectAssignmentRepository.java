package vut.fit.gja2023.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vut.fit.gja2023.app.entity.ProjectAssignmentBo;

/**
 * A repository used for saving/retrieving project assignments from the database.
 */
@Repository
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignmentBo, Long> {
}
