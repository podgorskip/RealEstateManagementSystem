package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.Broker;
import podgorskip.managementSystem.jpa.repositories.AgentsRepository;
import podgorskip.managementSystem.jpa.repositories.BrokersRepository;
import podgorskip.managementSystem.jpa.repositories.RolesRepository;
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

    @PostMapping("/add-agent")
    public ResponseEntity<String> addAgent(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestUserDTO user) {
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADD_AGENT"))) {

            Agent agent = new Agent();
            agent.setFirstName(user.getFirstName());
            agent.setLastName(user.getLastName());
            agent.setUsername(user.getUsername());
            agent.setPassword(passwordEncoder.encode(userDetails.getUsername()));
            agent.setRole(rolesRepository.findByName("ROLE_AGENT"));
            agent.setCreated(new Date());

            agentsRepository.save(agent);
            return ResponseEntity.ok("Correctly added an agent.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("You are not authorized to create a new agent account.");
    }

    @PostMapping("/add-broker")
    public ResponseEntity<String> addBroker(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody RequestUserDTO user) {

        if (Objects.isNull(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("No user details are provided.");
        }

        if (!user.validateData()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body("Not all the requirements are met.");
        }

        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADD_BROKER"))) {
            Broker broker = new Broker();
            broker.setFirstName(user.getFirstName());
            broker.setLastName(user.getLastName());
            broker.setUsername(user.getUsername());
            broker.setPassword(passwordEncoder.encode(userDetails.getUsername()));
            broker.setRole(rolesRepository.findByName("ROLE_BROKER"));
            broker.setCreated(new Date());

            brokersRepository.save(broker);
            return ResponseEntity.ok("Correctly added a broker.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("You are not authorized to create a new agent account.");
    }
}
