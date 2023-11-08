package podgorskip.managementSystem.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {
    private final String username;
    private final String password;
}