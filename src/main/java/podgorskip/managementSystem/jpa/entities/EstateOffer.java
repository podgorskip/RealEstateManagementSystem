package podgorskip.managementSystem.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "temp_estates", schema = "real_estate")
@Data
public class EstateOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private Agent agent;

    @OneToOne
    private Owner owner;

    private String type;
    private int price;
    private int rooms;
    private int bathrooms;
    private String localization;
    private boolean garage;
    private int storeys;
    private String description;
}
