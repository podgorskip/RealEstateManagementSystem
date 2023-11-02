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
import podgorskip.managementSystem.dto.RequestUserDTO;
import podgorskip.managementSystem.dto.UsernameDTO;
import podgorskip.managementSystem.jpa.entities.Accountant;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.Broker;
import podgorskip.managementSystem.jpa.entities.User;
import podgorskip.managementSystem.jpa.repositories.*;
import podgorskip.managementSystem.security.CustomUserDetails;
import java.util.Date;
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
    private static final Logger log = LogManager.getLogger(AdminController.class);
    private enum Roles { ACCOUNTANT, AGENT, BROKER}

    @PostMapping("/add-agent")
    public ResponseEntity<String> addAgent(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestUserDTO user) {
        ResponseEntity<String> response = validateCredentials(userDetails, user, "ADD_AGENT", Roles.AGENT);

        if (Objects.isNull(response)) {
            agentsRepository.save((Agent) createUser(user, Roles.AGENT));

            log.info("Correctly created a new agent account");
            log.info("Agents database updated");

            return ResponseEntity.ok("Correctly created a new agent account");
        }

        return response;
    }

    @PostMapping("/add-broker")
    public ResponseEntity<String> addBroker(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestUserDTO user) {
        ResponseEntity<String> response = validateCredentials(userDetails, user, "ADD_BROKER", Roles.BROKER);

        if (Objects.isNull(response)) {
            brokersRepository.save((Broker) createUser(user, Roles.BROKER));

            log.info("Correctly created a new broker account");
            log.info("Brokers database updated");

            return ResponseEntity.ok("Correctly created a new broker account");
        }

        return response;
    }

    @PostMapping("/add-accountant")
    public ResponseEntity<String> addAccountant(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestUserDTO user) {
        ResponseEntity<String> response = validateCredentials(userDetails, user, "ADD_ACCOUNTANT", Roles.ACCOUNTANT);

        if (Objects.isNull(response)) {
            accountantsRepository.save((Accountant) createUser(user, Roles.ACCOUNTANT));

            log.info("Correctly created a new accountant account");
            log.info("Accountants database updated");

            return ResponseEntity.ok("Correctly created a new accountant account");
        }

        return response;
    }

    @DeleteMapping("/remove-agent")
    public ResponseEntity<String> removeAgent(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UsernameDTO username) {
        return removeUser(userDetails, username, "REMOVE_AGENT", Roles.AGENT);
    }

    @DeleteMapping("/remove-broker")
    public ResponseEntity<String> removeBroker(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UsernameDTO username) {
        return removeUser(userDetails, username, "REMOVE_BROKER", Roles.BROKER);
    }

    @DeleteMapping("/remove-accountant")
    public ResponseEntity<String> removeAccountant(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UsernameDTO username) {
        return removeUser(userDetails, username, "REMOVE_ACCOUNTANT", Roles.ACCOUNTANT);
    }

    private User createUser(RequestUserDTO requestUser, Roles roleName) {
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

        return user;
    }

    private ResponseEntity<String> validateCredentials(CustomUserDetails userDetails, RequestUserDTO requestUser, String requiredAuthority, Roles roleName) {

        if (Objects.isNull(userDetails) || userDetails.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredAuthority))) {
            log.warn("Authenticated user lacked privilege {} to perform the request", requiredAuthority);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("You are not authorized to create a new " + roleName.name().toLowerCase() + " account.");
        }

        if (Objects.isNull(requestUser)) {
            log.warn("No credentials supplied to perform the request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("No user details are provided.");
        }

        if (!requestUser.validateData()) {
            log.warn("Provided credentials did not meet all the requirements");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Not all the requirements are met.");
        }

        return null;
    }

    private ResponseEntity<String> removeUser(CustomUserDetails userDetails, UsernameDTO username, String requiredAuthority, Roles roleName) {
        if (Objects.isNull(userDetails) || userDetails.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredAuthority))) {
            log.warn("Authenticated user lacked privilege {} to perform the request", requiredAuthority);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("You are not authorized to remove a(n) " + roleName.name().toLowerCase() + " account.");
        }

        if (Objects.isNull(username.getUsername())) {
            log.warn("No username account specified for deletion");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("No username specified.");
        }

        switch (roleName) {
            case AGENT -> {
                Agent agent = agentsRepository.findByUsername(username.getUsername());

                if (Objects.isNull(agent)) {
                    return accountNotFoundResponseEntity();
                }

                agentsRepository.delete(agent);
            }
            case BROKER -> {
                Broker broker = brokersRepository.findByUsername(username.getUsername());

                if (Objects.isNull(broker)) {
                    return accountNotFoundResponseEntity();
                }

                brokersRepository.delete(broker);
            }
            case ACCOUNTANT -> {
                Accountant accountant = accountantsRepository.findByUsername(username.getUsername());

                if (Objects.isNull(accountant)) {
                    return accountNotFoundResponseEntity();
                }

                accountantsRepository.delete(accountant);
            }
            default -> {
                log.warn("Specified role didn't suit any allowed for deletion");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        String message = "Correctly removed the account of type " + roleName;
        log.info(message);
        log.info("Database updated");

        return ResponseEntity.ok(message);
    }

    private ResponseEntity<String> accountNotFoundResponseEntity() {
        String message = "No account of the specified username found";
        log.warn(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
