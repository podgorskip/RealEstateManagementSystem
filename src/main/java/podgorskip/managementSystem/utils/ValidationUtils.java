package podgorskip.managementSystem.utils;

import java.util.Objects;

public class ValidationUtils {
    private static final int USERNAME_LENGTH = 20;
    private static final int PASSWORD_LENGTH = 20;
    private static final int FIRSTNAME_LENGTH = 20;
    private static final int LASTNAME_LENGTH = 20;

    public Boolean isUsernameValid(String username) {
        return Objects.nonNull(username) && username.length() <= USERNAME_LENGTH;
    }

    public Boolean isPasswordValid(String password) {
        return Objects.nonNull(password) && password.length() <= PASSWORD_LENGTH;
    }

    public Boolean isFirstNameValid(String firstName) {
        return Objects.nonNull(firstName) && firstName.length() <= FIRSTNAME_LENGTH;
    }

    public Boolean isLastNameValid(String lastName) {
        return Objects.nonNull(lastName) && lastName.length() <= LASTNAME_LENGTH;
    }
}
