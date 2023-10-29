package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Table(name = "accountants", schema = "real-estate")
@Data
public class Accountant {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    @Setter(AccessLevel.NONE)
    private Role role = Role.ACCOUNTANT;
}
