package podgorskip.managementSystem.utils;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
@NoArgsConstructor
public class PasswordGenerator {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuwxyz";
    private static final String UPPERCASE = LOWERCASE.toUpperCase();
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()?_-~";

    public static String generatePassword(int length, boolean useLowercase, boolean useUppercase, boolean useNumbers, boolean useSymbols) {
        String character = setRequirements(useLowercase, useUppercase, useNumbers, useSymbols);

        SecureRandom random = new SecureRandom();
        StringBuilder passwordBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            passwordBuilder.append(character.charAt(random.nextInt(character.length())));
        }

        return passwordBuilder.toString();
    }

    private static String setRequirements(boolean useLowercase, boolean useUppercase, boolean useNumbers, boolean useSymbols) {
        StringBuilder builder = new StringBuilder();

        if (useLowercase) builder.append(LOWERCASE);
        if (useUppercase) builder.append(UPPERCASE);
        if (useNumbers) builder.append(NUMBERS);
        if (useSymbols) builder.append(SYMBOLS);

        return builder.toString();
    }
}
