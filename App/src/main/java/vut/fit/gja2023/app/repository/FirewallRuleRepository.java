package vut.fit.gja2023.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vut.fit.gja2023.app.entity.FirewallRuleBo;

/**
 * A repository used for saving/retrieving firewall rules from the database.
 */
@Repository
public interface FirewallRuleRepository extends JpaRepository<FirewallRuleBo, Long> {
}
