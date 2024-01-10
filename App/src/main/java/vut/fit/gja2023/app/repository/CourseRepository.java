package vut.fit.gja2023.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vut.fit.gja2023.app.entity.CourseBo;
import vut.fit.gja2023.app.entity.TeamBo;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseBo, Long> {
    @Query("SELECT c FROM CourseBo c WHERE c.name = :courseName")
    Optional<TeamBo> findByCourseName(@Param("courseName") String courseName);
}
