package podgorskip.managementSystem.dto;

import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
public class EstateDTO {
    private Integer id;
    private String description;
    private String type;
    private Integer price;
    private Integer rooms;
    private Integer bathrooms;
    private String localization;
    private Boolean garage;
    private Integer storeys;
    private Date published;
    private String agentFirstName;
    private String agentLastName;
    private String agentEmail;
    private String agentPhoneNumber;

    public boolean validateData() {
        return Objects.nonNull(id) && Objects.nonNull(description) && Objects.nonNull(type) &&
                Objects.nonNull(price) && Objects.nonNull(rooms) && Objects.nonNull(bathrooms) &&
                Objects.nonNull(localization) && Objects.nonNull(garage) && Objects.nonNull(storeys);
    }
}
