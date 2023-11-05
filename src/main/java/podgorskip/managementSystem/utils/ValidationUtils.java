package podgorskip.managementSystem.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.security.DatabaseUserDetailsService;

import java.util.NoSuchElementException;
import java.util.Objects;

@Component
public class ValidationUtils {
    @Autowired
    private DatabaseUserDetailsService databaseUserDetailsService;
    private static final Logger log = LogManager.getLogger(ValidationUtils.class);

    public Boolean isUsernameAvailable(String username) {

        try { databaseUserDetailsService.loadUserByUsername(username); }
        catch (NoSuchElementException e) { return true; }

        log.warn("Provided username is already taken.");

        return false;
    }

    public Boolean isUserUnauthorized(CustomUserDetails userDetails, Privileges requiredAuthority) {

        if (Objects.isNull(userDetails) || userDetails.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredAuthority.name()))) {
            log.warn("Authenticated user lacked privilege {} to perform the request", requiredAuthority);
            return true;
        }

        return false;
    }
}
