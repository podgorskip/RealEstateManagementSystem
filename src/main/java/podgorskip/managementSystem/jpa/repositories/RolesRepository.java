package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
