package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sessions", schema = "real_estate")
@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sessionID;

    @OneToOne
    private User user;
}
