package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.Client;

@Repository
public interface ClientsRepository extends JpaRepository<Client, Integer> {
    Client findByUsername(String username);
}
