package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.Accountant;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.User;

import java.util.Optional;

@Repository
public interface AgentsRepository extends JpaRepository<Agent, Integer> {
    Optional<Agent> findByUsername(String username);
}
