package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.Broker;

@Repository
public interface BrokersRepository extends JpaRepository<Broker, Integer> {
}
