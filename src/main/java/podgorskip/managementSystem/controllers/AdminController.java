package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import podgorskip.managementSystem.dto.UserRequest;
import podgorskip.managementSystem.jpa.entities.Accountant;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.Broker;
import podgorskip.managementSystem.jpa.entities.User;
import podgorskip.managementSystem.jpa.repositories.*;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.utils.Privileges;
import podgorskip.managementSystem.utils.Roles;
import podgorskip.managementSystem.utils.ValidationUtils;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/real-estate-agency/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final AgentsRepository agentsRepository;
    private final BrokersRepository brokersRepository;
    private final AccountantsRepository accountantsRepository;
    private final ValidationUtils validationUtils;
    private static final Logger log = LogManager.getLogger(AdminController.class);

    @PostMapping("/add-agent")
    public ResponseEntity<String> addAgent(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserRequest user) {

        ResponseEntity<String> response = validateCredentials(userDetails, user, Privileges.ADD_AGENT, Roles.AGENT);

        if (Objects.isNull(response)) {
            agentsRepository.save((Agent) createUser(user, Roles.AGENT));

            log.info("Correctly created a new agent account");
            log.info("Agents database updated");

            return ResponseEntity.ok("Correctly created a new agent account");
        }

        return response;
    }

    @PostMapping("/add-broker")
    public ResponseEntity<String> addBroker(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserRequest user) {

        ResponseEntity<String> response = validateCredentials(userDetails, user, Privileges.ADD_BROKER, Roles.BROKER);

        if (Objects.isNull(response)) {
            brokersRepository.save((Broker) createUser(user, Roles.BROKER));

            log.info("Correctly created a new broker account");
            log.info("Brokers database updated");

            return ResponseEntity.ok("Correctly created a new broker account");
        }

        return response;
    }

    @PostMapping("/add-accountant")
    public ResponseEntity<String> addAccountant(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserRequest user) {

        ResponseEntity<String> response = validateCredentials(userDetails, user, Privileges.ADD_ACCOUNTANT, Roles.ACCOUNTANT);

        if (Objects.isNull(response)) {
            accountantsRepository.save((Accountant) createUser(user, Roles.ACCOUNTANT));

            log.info("Correctly created a new accountant account");
            log.info("Accountants database updated");

            return ResponseEntity.ok("Correctly created a new accountant account");
        }

        return response;
    }

    @DeleteMapping("/remove-agent")
    public ResponseEntity<String> removeAgent(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody Map<String, String> username) {
        return removeUser(userDetails, username.get("username"), Privileges.REMOVE_AGENT, Roles.AGENT);
    }

    @DeleteMapping("/remove-broker")
    public ResponseEntity<String> removeBroker(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody Map<String, String> username) {
        return removeUser(userDetails, username.get("username"), Privileges.REMOVE_BROKER, Roles.BROKER);
    }

    @DeleteMapping("/remove-accountant")
    public ResponseEntity<String> removeAccountant(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody Map<String, String> username) {
        return removeUser(userDetails, username.get("username"), Privileges.REMOVE_ACCOUNTANT, Roles.ACCOUNTANT);
    }

    private User createUser(UserRequest requestUser, Roles roleName) {

        User user;

        switch (roleName) {
            case AGENT -> user = new Agent();
            case BROKER -> user = new Broker();
            case ACCOUNTANT -> user = new Accountant();
            default -> {
                String message = "Specified role didn't match any available roles";
                log.error(message);
                throw new RuntimeException(message);
            }
        }

        user.setFirstName(requestUser.getFirstName());
        user.setLastName(requestUser.getLastName());
        user.setUsername(requestUser.getUsername());
        user.setPassword(passwordEncoder.encode(requestUser.getUsername()));
        user.setRole(rolesRepository.findByName("ROLE_" + roleName.name()));
        user.setCreated(new Date());
        user.setEmail(requestUser.getEmail());
        user.setPhoneNumber(requestUser.getPhoneNumber());

        return user;
    }

    private ResponseEntity<String> validateCredentials(CustomUserDetails userDetails, UserRequest requestUser, Privileges requiredAuthority, Roles roleName) {

        if (validationUtils.isUserUnauthorized(userDetails, requiredAuthority)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body("You are not authorized to create a new " + roleName.name().toLowerCase() + " account.");
        }

        if (Objects.isNull(requestUser)) {
            log.warn("No credentials supplied to perform the request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("No user details are provided.");
        }

        if (!validationUtils.isUsernameAvailable(requestUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Provided username is already taken");
        }

        if (!requestUser.validateData()) {
            log.warn("Provided credentials did not meet all the requirements");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Not all the requirements are met.");
        }

        return null;
    }

    private ResponseEntity<String> removeUser(CustomUserDetails userDetails, String username, Privileges requiredAuthority, Roles roleName) {

        if (validationUtils.isUserUnauthorized(userDetails, requiredAuthority)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body("You are not authorized to remove a(n) " + roleName.name().toLowerCase() + " account.");
        }

        if (Objects.isNull(username)) {
            log.warn("No username account specified for deletion");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("No username specified.");
        }

        if (validationUtils.isUsernameAvailable(username)) {
            String message = "No account of the specified username found";
            log.warn(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }

        switch (roleName) {
            case AGENT -> {
                Agent agent = agentsRepository.findByUsername(username);
                agentsRepository.delete(agent);
            }
            case BROKER -> {
                Broker broker = brokersRepository.findByUsername(username);
                brokersRepository.delete(broker);
            }
            case ACCOUNTANT -> {
                Accountant accountant = accountantsRepository.findByUsername(username);
                accountantsRepository.delete(accountant);
            }
            default -> {
                log.warn("Specified role didn't suit any allowed for deletion");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        String message = "Correctly removed the account of type " + roleName.name().toLowerCase();
        log.info(message);
        log.info("Database updated");

        return ResponseEntity.ok(message);
    }
}
