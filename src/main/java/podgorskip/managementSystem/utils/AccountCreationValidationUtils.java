package podgorskip.managementSystem.utils;

import java.util.Objects;

public class AccountCreationValidationUtils {
    private static final int USERNAME_LENGTH = 10;
    private static final int PASSWORD_LENGTH = 20;
    private static final int FIRSTNAME_LENGTH = 20;
    private static final int LASTNAME_LENGTH = 20;
    private static final int PHONE_NUMBER_LENGTH = 15;
    private static final int EMAIL_LENGTH = 30;

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

    public Boolean isPhoneNumberValid(String phoneNumber) {
        return Objects.nonNull(phoneNumber) && phoneNumber.length() <= PHONE_NUMBER_LENGTH;
    }

    public Boolean isEmailValid(String email) {
        return Objects.nonNull(email) && email.contains("@") && email.length() <= EMAIL_LENGTH;
    }
}
