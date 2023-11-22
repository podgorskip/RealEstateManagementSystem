package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "scheduled_meetings", schema = "real_estate")
public class ScheduledMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    private Date date;
}
