package podgorskip.managementSystem.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import podgorskip.managementSystem.jpa.entities.Estate;

import java.util.Date;

@Data
public class EstateDTO {
    private int id;
    private String description;
    private String type;
    private int price;
    private int rooms;
    private int bathrooms;
    private String localization;
    private boolean garage;
    private int storeys;
    private Date published;

    private String agentFirstName;
    private String agentLastName;
    private String agentEmail;
    private String agentPhoneNumber;

    public EstateDTO(String description, String type, int price, int rooms, int bathrooms, String localization, boolean garage, int storeys) {
        this.description = description;
        this.type = type;
        this.price = price;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.localization = localization;
        this.garage = garage;
        this.storeys = storeys;
    }

    public EstateDTO(Estate estate) {
        setId(estate.getId());
        setType(estate.getType());
        setPrice(estate.getPrice());
        setLocalization(estate.getLocalization());
        setDescription(estate.getDescription());
        setRooms(estate.getRooms());
        setBathrooms(estate.getBathrooms());
        setGarage(estate.isGarage());
        setStoreys(estate.getStoreys());
        setPublished(estate.getPublished());
        setAgentFirstName(estate.getAgent().getFirstName());
        setAgentLastName(estate.getAgent().getLastName());
        setAgentEmail(estate.getAgent().getEmail());
        setAgentPhoneNumber(estate.getAgent().getPhoneNumber());
    }
}
