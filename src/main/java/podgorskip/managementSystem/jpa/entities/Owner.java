package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "owners", schema = "real_estate")
@Data
public class Owner {
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

    @OneToMany(mappedBy = "owner")
    private List<Estate> ownedEstates;

    @Setter(AccessLevel.NONE)
    private Role role = Role.OWNER;

    @Setter(AccessLevel.NONE)
    private Date created;
}
