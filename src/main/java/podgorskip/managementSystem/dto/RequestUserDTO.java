package podgorskip.managementSystem.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import podgorskip.managementSystem.utils.ValidationUtils;

@Data
@RequiredArgsConstructor
public class RequestUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String role;
    private static final ValidationUtils validationUtils = new ValidationUtils();

    public Boolean validateData() {
        return validationUtils.isFirstNameValid(firstName) && validationUtils.isLastNameValid(lastName) &&
                validationUtils.isUsernameValid(username) && validationUtils.isPasswordValid(password);
    }
}
