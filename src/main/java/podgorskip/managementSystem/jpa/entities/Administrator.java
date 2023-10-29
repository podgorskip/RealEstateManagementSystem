package podgorskip.managementSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;

@Entity
@Table(name = "admins", schema = "real_estate")
@JsonTypeName("admin")
public class Administrator extends User {
    private Administrator() {
        setRole(Role.ADMIN);
    }
}
