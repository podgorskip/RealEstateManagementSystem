package podgorskip.managementSystem.dto;

import lombok.Data;

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
}
