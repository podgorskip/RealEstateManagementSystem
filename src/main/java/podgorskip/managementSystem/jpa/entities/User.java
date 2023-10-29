package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Entity
@Table(name = "users", schema = "real_estate")
@Data
@Component
public class User {
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
    private Date created;

    @Setter(AccessLevel.NONE)
    private Role role = Role.USER;
}
