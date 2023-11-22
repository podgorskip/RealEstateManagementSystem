package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "available_meetings", schema = "real_estate")
@Data
public class AvailableMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    private Date date;
}
