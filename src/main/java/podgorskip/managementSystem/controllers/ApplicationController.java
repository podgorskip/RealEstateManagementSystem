package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.Estate;
import podgorskip.managementSystem.jpa.repositories.AgentsRepository;
import podgorskip.managementSystem.jpa.repositories.EstatesRepository;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.utils.Privileges;
import podgorskip.managementSystem.utils.ValidationUtils;

import java.util.List;

@RestController
@RequestMapping("/real-estate-agency")
@RequiredArgsConstructor
public class ApplicationController {
    private final AgentsRepository agentsRepository;
    private final EstatesRepository estatesRepository;
    private final ValidationUtils validationUtils;
    private static final Logger log = LogManager.getLogger(ApplicationController.class);
    @GetMapping("/available-agents")
    public ResponseEntity<List<Agent>> availableAgents(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.CHECK_AGENTS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).build();
        }

        log.info("Available agents returned");

        return ResponseEntity.ok(agentsRepository.findAll());
    }

    @GetMapping("/available-estates")
    public ResponseEntity<List<Estate>> availableEstates(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.CHECK_ESTATES)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).build();
        }

        log.info("Available estates returned");

        return ResponseEntity.ok(estatesRepository.findAll());
    }
}
