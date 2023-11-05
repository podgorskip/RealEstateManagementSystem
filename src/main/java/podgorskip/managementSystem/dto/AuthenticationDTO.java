package podgorskip.managementSystem.dto;

import lombok.Getter;

@Getter
public class AuthenticationDTO {
    private final String username;
    private final String password;
    private final String passwordMasked;

    public AuthenticationDTO(String username, String password) {
        this.username = username;
        this.password = password;
        this.passwordMasked = "*".repeat(password.length());
    }

    @Override
    public String toString() {
        return "AuthenticationDTO[username=" + username + ", password=" + passwordMasked + "]";
    }
}