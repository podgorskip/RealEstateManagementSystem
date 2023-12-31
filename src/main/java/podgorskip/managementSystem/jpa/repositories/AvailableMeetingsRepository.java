package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.AvailableMeeting;

import java.util.List;

@Repository
public interface AvailableMeetingsRepository extends JpaRepository<AvailableMeeting, Integer> {
    List<AvailableMeeting> findByAgent(Agent agent);
}
