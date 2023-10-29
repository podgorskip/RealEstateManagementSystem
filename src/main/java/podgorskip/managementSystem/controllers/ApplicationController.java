package podgorskip.managementSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.repositories.AgentsRepository;

import java.util.Optional;

@RestController
@RequestMapping("/real-estate-agency")
public class ApplicationController {

    @Autowired
    private AgentsRepository agentsRepository;

    @GetMapping("/agents")
    public ResponseEntity<Agent> showAgentDetails(@RequestBody String username) {
        Optional<Agent> agent = agentsRepository.findByUsername(username);

        try {

            if (agent.isEmpty()) throw new UsernameNotFoundException("User not found.");

        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }

        return ResponseEntity.ok(agent.get());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("OK");
    }
}
