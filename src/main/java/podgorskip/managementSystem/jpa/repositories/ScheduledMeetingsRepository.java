package podgorskip.managementSystem.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import podgorskip.managementSystem.jpa.entities.ScheduledMeeting;

@Repository
public interface ScheduledMeetingsRepository extends JpaRepository<ScheduledMeeting, Integer> {
}
