package podgorskip.managementSystem.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "estates", schema = "real_estate")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Estate {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    private String type;
    private int price;
    private int rooms;
    private int bathrooms;
    private String localization;
    private boolean garage;
    private int storeys;

    @Setter(AccessLevel.NONE)
    private Date published;

    private String description;

    @Column(name = "expiration_date")
    private Date expirationDate;
}
