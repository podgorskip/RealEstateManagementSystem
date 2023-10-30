package podgorskip.managementSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "clients", schema = "real_estate")
@Component
@JsonTypeName("client")
public class Client extends User {

}
