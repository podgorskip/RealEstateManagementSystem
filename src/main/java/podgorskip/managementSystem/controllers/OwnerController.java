package podgorskip.managementSystem.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import podgorskip.managementSystem.annotations.RequiredPrivilege;
import podgorskip.managementSystem.dto.EstateDTO;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.EstateOffer;
import podgorskip.managementSystem.jpa.entities.Owner;
import podgorskip.managementSystem.jpa.repositories.AgentsRepository;
import podgorskip.managementSystem.jpa.repositories.EstateOfferRepository;
import podgorskip.managementSystem.jpa.repositories.OwnersRepository;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.utils.Privileges;
import podgorskip.managementSystem.utils.ValidationUtils;

import java.util.Optional;

@RestController
@RequestMapping("/real-estate-agency")
@RequiredArgsConstructor
public class OwnerController {
    private final EstateOfferRepository estateOfferRepository;
    private final AgentsRepository agentsRepository;
    private final OwnersRepository ownersRepository;
    private static final Logger log = LogManager.getLogger(OwnerController.class);

    @PostMapping("/report-offer")
    @Transactional
    @RequiredPrivilege(value = Privileges.REPORT_OFFER)
    public ResponseEntity<String> reportOffer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody EstateDTO requestEstate,
            @RequestParam("id") Integer agentID
    ) {

        if (!requestEstate.validateData()) {
            log.warn("Offer not reported. Provided data didn't include all the required attributes");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provided data didn't include all the required attributes");
        }

        Optional<Agent> agent = agentsRepository.findById(agentID);
        Owner owner = ownersRepository.findByUsername(userDetails.getUsername());

        if (agent.isEmpty()) {
            log.warn("Offer not reported. No agent of the provided id {} found", agentID);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No agent of the provided id found");
        }

        EstateOffer estateOffer = new EstateOffer();

        estateOffer.setAgent(agent.get());
        estateOffer.setOwner(owner);
        estateOffer.setType(requestEstate.getType());
        estateOffer.setPrice(requestEstate.getPrice());
        estateOffer.setRooms(requestEstate.getRooms());
        estateOffer.setBathrooms(requestEstate.getBathrooms());
        estateOffer.setLocalization(requestEstate.getLocalization());
        estateOffer.setGarage(requestEstate.getGarage());
        estateOffer.setStoreys(requestEstate.getStoreys());
        estateOffer.setDescription(requestEstate.getDescription());

        estateOfferRepository.save(estateOffer);

        log.info("Offer reported and saved to the temporary database");

        return ResponseEntity.ok("Successfully sent the offer");
    }
}
