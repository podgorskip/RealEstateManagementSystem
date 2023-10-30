package podgorskip.managementSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;

@Entity
@Table(name = "accountants", schema = "real_estate")
@JsonTypeName("accountant")
public class Accountant extends User {

}
