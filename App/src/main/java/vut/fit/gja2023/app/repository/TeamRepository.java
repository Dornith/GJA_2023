package vut.fit.gja2023.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vut.fit.gja2023.app.entity.TeamBo;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TeamBo, Long> {
    @Query("SELECT t FROM TeamBo t WHERE t.name = :teamName")
    Optional<TeamBo> findByTeamName(@Param("teamName") String teamName);
}
