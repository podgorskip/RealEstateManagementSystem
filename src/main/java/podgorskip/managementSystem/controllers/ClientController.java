package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.repositories.AgentsRepository;
import podgorskip.managementSystem.security.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/real-estate-agency/client")
@RequiredArgsConstructor
public class ClientController {
    private final AgentsRepository agentsRepository;
//
//    @GetMapping("/available-agents")
//    public List<Agent> availableAgents(@AuthenticationPrincipal CustomUserDetails userDetails) {
//
//    }
}
