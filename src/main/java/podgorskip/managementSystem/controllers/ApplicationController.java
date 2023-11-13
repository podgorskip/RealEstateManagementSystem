package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import podgorskip.managementSystem.dto.AgentDTO;
import podgorskip.managementSystem.dto.PasswordChangeRequest;
import podgorskip.managementSystem.dto.mappers.AgentMapper;
import podgorskip.managementSystem.dto.EstateDTO;
import podgorskip.managementSystem.dto.mappers.EstateMapper;
import podgorskip.managementSystem.jpa.entities.*;
import podgorskip.managementSystem.jpa.repositories.*;
import podgorskip.managementSystem.jpa.repositories.specification.EstatesSpecification;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.utils.Privileges;
import podgorskip.managementSystem.utils.ValidationUtils;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/real-estate-agency")
@RequiredArgsConstructor
public class ApplicationController {
    private final AgentsRepository agentsRepository;
    private final AccountantsRepository accountantsRepository;
    private final AdministratorsRepository administratorsRepository;
    private final BrokersRepository brokersRepository;
    private final ClientsRepository clientsRepository;
    private final OwnersRepository ownersRepository;
    private final EstatesRepository estatesRepository;
    private final ValidationUtils validationUtils;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LogManager.getLogger(ApplicationController.class);
    @GetMapping("/agents")
    public ResponseEntity<List<AgentDTO>> availableAgents(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.CHECK_AGENTS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).build();
        }

        List<Agent> agents = agentsRepository.findAll();

        if (agents.isEmpty()) {
            log.info("No records returned. No available agents were found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        log.info("Available agents returned");
        return ResponseEntity.ok(agents.stream().map(AgentMapper.INSTANCE::convert).toList());
    }

    @GetMapping("/estates")
    public ResponseEntity<List<EstateDTO>> estates(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.CHECK_ESTATES)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).build();
        }

        List<Estate> estates = estatesRepository.findAll();

        if (estates.isEmpty()) {
            log.info("No records returned. No available estates were found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
        }

        log.info("Available estates mapped and returned");
        return ResponseEntity.ok(estates.stream().map(EstateMapper.INSTANCE::convert).toList());
    }

    @GetMapping("/estates-filter")
    public ResponseEntity<List<EstateDTO>> estatesFiltered(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "priceFrom", required = false) Integer priceFrom,
            @RequestParam(name = "priceTo", required = false) Integer priceTo,
            @RequestParam(name = "rooms", required = false) Integer rooms,
            @RequestParam(name = "bathrooms", required = false) Integer bathrooms,
            @RequestParam(name = "localization", required = false) String localization,
            @RequestParam(name = "garage", required = false) Boolean garage,
            @RequestParam(name = "storeys", required = false) Integer storeys
    ) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.CHECK_ESTATES)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).build();
        }

        Specification<Estate> spec = EstatesSpecification.filteredEstates(type, priceFrom, priceTo, rooms, bathrooms, localization, garage, storeys);
        List<Estate> estates = estatesRepository.findAll(spec);

        log.info("Filters applied");

        if (estates.isEmpty()) {
            log.info("No records returned. No available estates were found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
        }

        log.info("Available estates mapped and returned");
        return ResponseEntity.ok(estates.stream().map(EstateMapper.INSTANCE::convert).toList());
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody PasswordChangeRequest passwords) {

        if (passwords.getNewPassword().isEmpty() || passwords.getOldPassword().isEmpty()) {
            log.warn("Update rejected. Required passwords weren't provided correctly");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Neither old password nor new password cannot be empty");
        }

        if (!passwordEncoder.matches(passwords.getOldPassword(), userDetails.getPassword())) {
            log.warn("Update rejected. Provided password didn't match the current one");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Provided password didn't match the current one");
        }

        if (passwordEncoder.matches(passwords.getNewPassword(), userDetails.getPassword())) {
            log.warn("Update rejected. Password was the same as the current one");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("New password cannot be the same as the current one");
        }

        switch (userDetails.getRole()) {
            case AGENT -> {
                Agent agent = agentsRepository.findByUsername(userDetails.getUsername());
                agent.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
                agentsRepository.save(agent);
            }
            case ACCOUNTANT -> {
                Accountant accountant = accountantsRepository.findByUsername(userDetails.getUsername());
                accountant.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
                accountantsRepository.save(accountant);
            }
            case ADMIN -> {
                Administrator administrator = administratorsRepository.findByUsername(userDetails.getUsername());
                administrator.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
                administratorsRepository.save(administrator);
            }
            case BROKER -> {
                Broker broker = brokersRepository.findByUsername(userDetails.getUsername());
                broker.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
                brokersRepository.save(broker);
            }
            case CLIENT -> {
                Client client = clientsRepository.findByUsername(userDetails.getUsername());
                client.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
                clientsRepository.save(client);
            }
            case OWNER -> {
                Owner owner = ownersRepository.findByUsername(userDetails.getUsername());
                owner.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
                ownersRepository.save(owner);
            }
        }

        log.info("Password changed correctly");

        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
    }

    @PatchMapping("/change-username")
    public ResponseEntity<String> changeUsername(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody Map<String, String> newUsername) {

        String username = newUsername.get("username");

        if (username.isEmpty()) {
            log.warn("Update rejected. New username cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be empty");
        }

        if (!validationUtils.isUsernameAvailable(username)) {
            log.warn("Update rejected. Provided username is already taken");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username unavailable");
        }

        switch (userDetails.getRole()) {
            case AGENT -> {
                Agent agent = agentsRepository.findByUsername(userDetails.getUsername());
                agent.setUsername(username);
                agentsRepository.save(agent);
            }
            case ACCOUNTANT -> {
                Accountant accountant = accountantsRepository.findByUsername(userDetails.getUsername());
                accountant.setUsername(username);
                accountantsRepository.save(accountant);
            }
            case ADMIN -> {
                Administrator administrator = administratorsRepository.findByUsername(userDetails.getUsername());
                administrator.setUsername(username);
                administratorsRepository.save(administrator);
            }
            case BROKER -> {
                Broker broker = brokersRepository.findByUsername(userDetails.getUsername());
                broker.setUsername(username);
                brokersRepository.save(broker);
            }
            case CLIENT -> {
                Client client = clientsRepository.findByUsername(userDetails.getUsername());
                client.setUsername(username);
                clientsRepository.save(client);
            }
            case OWNER -> {
                Owner owner = ownersRepository.findByUsername(userDetails.getUsername());
                owner.setUsername(username);
                ownersRepository.save(owner);
            }
        }

        log.info("Username updated correctly");

        return ResponseEntity.ok("Username updated correctly");
    }
}
