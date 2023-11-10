package podgorskip.managementSystem.dto;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import podgorskip.managementSystem.utils.AccountCreationValidationUtils;

@Data
public class RequestUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private static final Logger log = LogManager.getLogger(RequestUserDTO.class);
    private static final AccountCreationValidationUtils validationUtils = new AccountCreationValidationUtils();

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
            log.warn("Provided username didn't align with the requirements.");
            return false;
        }

        if (!validationUtils.isPasswordValid(password)) {
            log.warn("Provided password didn't align with the requirements.");
            return false;
        }

        if (!validationUtils.isPhoneNumberValid(phoneNumber)) {
            log.warn("Provided phone number didn't align with the requirements.");
            return false;
        }

        if (!validationUtils.isEmailValid(email)) {
            log.warn("Provided email didn't align with the requirements.");
            return false;
        }

        return true;
    }

}
