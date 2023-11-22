package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "available_meetings", schema = "real_estate")
public class AvailableMeetings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    private Date date;
}
