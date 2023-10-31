package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import podgorskip.managementSystem.dto.RequestUserDTO;
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

    @PostMapping("/add-agent")
    public ResponseEntity<String> addAgent(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestUserDTO user) {
        ResponseEntity<String> response = validateCredentials(userDetails, user, "ADD_AGENT", "AGENT");

        if (Objects.isNull(response)) {
            agentsRepository.save((Agent) createUser(user, "AGENT"));
            return ResponseEntity.ok("Correctly created a new agent account");
        }

        return response;
    }

    @PostMapping("/add-broker")
    public ResponseEntity<String> addBroker(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestUserDTO user) {
        ResponseEntity<String> response = validateCredentials(userDetails, user, "ADD_BROKER", "BROKER");

        if (Objects.isNull(response)) {
            brokersRepository.save((Broker) createUser(user, "BROKER"));
            return ResponseEntity.ok("Correctly created a new broker account");
        }

        return response;
    }

    @PostMapping("/add-accountant")
    public ResponseEntity<String> addAccountant(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestUserDTO user) {
        ResponseEntity<String> response = validateCredentials(userDetails, user, "ADD_ACCOUNTANT", "ACCOUNTANT");

        if (Objects.isNull(response)) {
            accountantsRepository.save((Accountant) createUser(user, "ACCOUNTANT"));
            return ResponseEntity.ok("Correctly created a new accountant account");
        }

        return response;
    }

    private User createUser(RequestUserDTO requestUser, String roleName) {

        User user;

        if ("AGENT".equals(roleName)) user = new Agent();
        else if ("BROKER".equals(roleName)) user = new Broker();
        else if ("ACCOUNTANT".equals(roleName)) user = new Accountant();
        else throw new RuntimeException("Specified role doesn't match any available roles");

        user.setFirstName(requestUser.getFirstName());
        user.setLastName(requestUser.getLastName());
        user.setUsername(requestUser.getUsername());
        user.setPassword(passwordEncoder.encode(requestUser.getUsername()));
        user.setRole(rolesRepository.findByName("ROLE_" + roleName.toUpperCase()));
        user.setCreated(new Date());

        return user;
    }

    private ResponseEntity<String> validateCredentials(CustomUserDetails userDetails, RequestUserDTO requestUser, String requiredAuthority, String roleName) {
        if (Objects.isNull(requestUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("No user details are provided.");
        }

        if (!requestUser.validateData()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Not all the requirements are met.");
        }

        if (userDetails.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(requiredAuthority))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("You are not authorized to create a new " + roleName.toLowerCase() + " account.");
        }

        return null;
    }
}
