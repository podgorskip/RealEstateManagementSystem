package podgorskip.managementSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.*;

@Entity
@Table(name = "brokers", schema = "real_estate")
@JsonTypeName("broker")
public class Broker extends User {

}
