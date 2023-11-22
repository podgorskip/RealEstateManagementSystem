package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.ScheduledMeetings;

@Repository
public interface ScheduledMeetingsRepository extends JpaRepository<ScheduledMeetings, Integer> {
}
