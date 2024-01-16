package vut.fit.gja2023.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vut.fit.gja2023.app.entity.ProjectBo;
import vut.fit.gja2023.app.entity.UserBo;

import java.util.List;

/**
 * A repository used for saving/retrieving projects from the database.
 */
@Repository
public interface ProjectRepository extends JpaRepository<ProjectBo, Long> {
}
