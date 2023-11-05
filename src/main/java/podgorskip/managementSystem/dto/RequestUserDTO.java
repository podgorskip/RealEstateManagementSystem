package podgorskip.managementSystem.dto;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import podgorskip.managementSystem.security.DatabaseUserDetailsService;
import podgorskip.managementSystem.utils.ValidationUtils;

import java.util.Objects;

@Data
public class RequestUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;
    private static final Logger log = LogManager.getLogger(RequestUserDTO.class);
    private static final ValidationUtils validationUtils = new ValidationUtils();

    public Boolean validateData() {

        if (!validationUtils.isFirstNameValid(firstName)) {
            log.warn("Provided first name didn't align with the requirements.");
            return false;
        }

        if (!validationUtils.isLastNameValid(lastName)) {
            log.warn("Provided last name didn't align with the requirements.");
            return false;
        }

        if (!validationUtils.isUsernameValid(username)) {
            log.warn("Provided username name didn't align with the requirements.");
            return false;
        }

        if (!validationUtils.isPasswordValid(password)) {
            log.warn("Provided password name didn't align with the requirements.");
            return false;
        }

        return true;
    }

}
