package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "accountants", schema = "real-estate")
@Data
public class Accountant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;
}
