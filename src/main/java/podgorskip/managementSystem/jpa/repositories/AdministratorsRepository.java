package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.Administrator;

@Repository
public interface AdministratorsRepository extends JpaRepository<Administrator, Integer> {
    Administrator findByUsername(String username);
}
