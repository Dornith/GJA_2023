package vut.fit.gja2023.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vut.fit.gja2023.app.entity.UserBo;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserBo, Long> {

    @Query("SELECT u FROM UserBo u WHERE u.name = :userName")
    List<UserBo> findByUserName(@Param("userName") String userName);
    
    @Query("SELECT u FROM UserBo u WHERE u.login = :userLogin")
    Optional<UserBo> findByUserLogin(@Param("userLogin") String userLogin);
}
