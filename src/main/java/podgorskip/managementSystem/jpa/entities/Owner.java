package podgorskip.managementSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "owners", schema = "real_estate")
@JsonTypeName("owner")
public class Owner extends User {

    @OneToMany(mappedBy = "owner")
    private List<Estate> ownedEstates;
}
