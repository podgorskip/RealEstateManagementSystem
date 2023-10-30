package podgorskip.managementSystem.dto;

import lombok.Data;

@Data
public class RequestUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;
}
