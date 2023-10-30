package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "privileges", schema = "real_estate")
@Getter
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
