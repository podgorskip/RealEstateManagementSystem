package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admins", schema = "real_estate")
@Data
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;

    private String password;
}
