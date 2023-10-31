package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.Accountant;

@Repository
public interface AccountantsRepository extends JpaRepository<Accountant, Integer> {
    Accountant findByUsername(String username);
}
