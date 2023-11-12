package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.Estate;
import podgorskip.managementSystem.jpa.entities.EstateOffer;
import podgorskip.managementSystem.jpa.entities.Role;
import podgorskip.managementSystem.jpa.repositories.AgentsRepository;
import podgorskip.managementSystem.jpa.repositories.EstateOfferRepository;
import podgorskip.managementSystem.jpa.repositories.EstatesRepository;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.utils.Privileges;
import podgorskip.managementSystem.utils.ValidationUtils;
import java.util.*;

@RestController
@RequestMapping("/real-estate-agency")
@RequiredArgsConstructor
public class AgentController {
    private final EstateOfferRepository estateOfferRepository;
    private final EstatesRepository estatesRepository;
    private final AgentsRepository agentsRepository;
    private final ValidationUtils validationUtils;
    private static final Logger log = LogManager.getLogger(AgentController.class);

    @GetMapping("/offers-to-be-posted")
    public ResponseEntity<List<EstateOffer>> checkOffersToBePosted(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.POST_OFFER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<EstateOffer> estateOffers = estateOfferRepository.findAll();

        List<EstateOffer> agentsOffers = estateOffers.stream()
                .filter(estateOffer -> estateOffer.getAgent().equals(agentsRepository.findByUsername(userDetails.getUsername())))
                .toList();

        if (agentsOffers.isEmpty()) {
            log.info("No offers to be posted by found");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(agentsOffers);
    }

    @PostMapping("/post-offer")
    public ResponseEntity<String> postOffer(
            @RequestParam("offerID") Integer offerID,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, Integer> expirationInterval
    ) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.POST_OFFER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<EstateOffer> estateOffer = estateOfferRepository.findById(offerID);

        if (estateOffer.isEmpty()) {
            log.warn("No offer of the provided id: {} found", offerID);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        EstateOffer offer = estateOffer.get();

        Estate estate = new Estate();
        estate.setAgent(offer.getAgent());
        estate.setOwner(offer.getOwner());
        estate.setType(offer.getType());
        estate.setPrice(offer.getPrice());
        estate.setRooms(offer.getRooms());
        estate.setBathrooms(offer.getBathrooms());
        estate.setLocalization(offer.getLocalization());
        estate.setGarage(offer.isGarage());
        estate.setStoreys(offer.getStoreys());
        estate.setPublished(new Date());
        estate.setDescription(offer.getDescription());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, expirationInterval.get("interval"));

        estate.setExpirationDate(calendar.getTime());

        estatesRepository.save(estate);
        log.info("Correctly posted the offer of id: {}", offerID);

        estateOfferRepository.delete(estateOffer.get());
        log.info("Offer removed from the temporary database");

        return ResponseEntity.ok("Correctly posted the offer");
    }

    @DeleteMapping("/delete-offer")
    public ResponseEntity<String> deleteOffer(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("id") Integer estateID) {

        if (validationUtils.isUserUnauthorized(userDetails, Privileges.DELETE_OFFER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete offers");
        }

        Optional<Estate> estateOptional = estatesRepository.findById(estateID);

        if (estateOptional.isEmpty()) {
            log.warn("Deletion rejected. No estate of the provided id was found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Estate of the provided id doesn't exist");
        }

        Estate estate = estateOptional.get();
        Agent agent = agentsRepository.findByUsername(userDetails.getUsername());

        if (!estate.getAgent().equals(agent)) {
            log.warn("Deletion rejected. Authenticated agent is not assigned to the specified estate");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deletion rejected. Requested estate is not managed by you");
        }

        estatesRepository.delete(estate);

        log.info("Deletion successful. Database updated");

        return ResponseEntity.ok("Successfully deleted the offer");
    }
}
