package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import podgorskip.managementSystem.dto.EstateDTO;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.Estate;
import podgorskip.managementSystem.jpa.repositories.AgentsRepository;
import podgorskip.managementSystem.jpa.repositories.EstatesRepository;
import podgorskip.managementSystem.jpa.repositories.specification.EstatesSpecification;
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
    @GetMapping("/agents")
    public ResponseEntity<List<Agent>> availableAgents(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.CHECK_AGENTS)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).build();
        }

        log.info("Available agents returned");

        return ResponseEntity.ok(agentsRepository.findAll());
    }

    @GetMapping("/estates")
    public ResponseEntity<List<EstateDTO>> estates(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.CHECK_ESTATES)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).build();
        }

        List<Estate> estates = estatesRepository.findAll();

        if (estates.isEmpty()) {
            log.info("No available estates records found.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
        }

        log.info("Available estates mapped and returned");
        return ResponseEntity.ok(estates.stream().map(EstateDTO::new).toList());
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
            log.info("No available estates records found.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
        }

        log.info("Available estates mapped and returned");
        return ResponseEntity.ok(estates.stream().map(EstateDTO::new).toList());
    }

}
