package vut.fit.gja2023.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vut.fit.gja2023.app.entity.ProjectBo;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectBo, Long> {
}
