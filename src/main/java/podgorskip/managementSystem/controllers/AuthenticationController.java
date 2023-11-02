package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import podgorskip.managementSystem.dto.AuthenticationDTO;
import podgorskip.managementSystem.dto.RequestUserDTO;
import podgorskip.managementSystem.jpa.entities.Client;
import podgorskip.managementSystem.jpa.entities.Owner;
import podgorskip.managementSystem.jpa.entities.Role;
import podgorskip.managementSystem.jpa.repositories.ClientsRepository;
import podgorskip.managementSystem.jpa.repositories.OwnersRepository;
import podgorskip.managementSystem.jpa.repositories.RolesRepository;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.security.JwtUtils;
import podgorskip.managementSystem.security.DatabaseUserDetailsService;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DatabaseUserDetailsService databaseUserDetailsService;
    private final JwtUtils jwtUtils;
    private final ClientsRepository clientsRepository;
    private final OwnersRepository ownersRepository;
    private final RolesRepository rolesRepository;
    private final static Logger log = LogManager.getLogger(AuthenticationController.class);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RequestUserDTO user) {
        Role role = rolesRepository.findByName(user.getRole());

        if (!user.validateData()) {
            String message = "Some of the expected criteria is not met for provided credentials: " + user;
            log.info(message);

            return ResponseEntity.status(204).contentType(MediaType.APPLICATION_JSON).body(message);
        }

        if (Objects.nonNull(role)) {

            if ("ROLE_CLIENT".equals(user.getRole())) {
                Client client = new Client();
                client.setFirstName(user.getFirstName());
                client.setLastName(user.getLastName());
                client.setUsername(user.getUsername());
                client.setPassword(passwordEncoder.encode(user.getPassword()));
                client.setCreated(new Date());
                client.setRole(role);

                clientsRepository.save(client);
                log.info("Correctly created a new client account.");
                log.info("Clients database updated.");

                return ResponseEntity.ok("Correctly registered as a client.");
            }

            if ("ROLE_OWNER".equals(user.getRole())) {
                Owner owner = new Owner();
                owner.setFirstName(user.getFirstName());
                owner.setLastName(user.getLastName());
                owner.setUsername(user.getUsername());
                owner.setPassword(passwordEncoder.encode(user.getPassword()));
                owner.setCreated(new Date());
                owner.setRole(role);

                ownersRepository.save(owner);
                log.info("Correctly created a new owner account.");
                log.info("Owners database updated.");

                return ResponseEntity.ok("Correctly registered as an owner.");
            }
        }

        String message = "Provided role: " + role + " doesn't match any available options.";
        log.warn(message);

        return ResponseEntity.status(204).body(message);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationDTO user) {
        log.info("Credentials to authenticate: " + user);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        CustomUserDetails customUserDetails = databaseUserDetailsService.loadUserByUsername(user.getUsername());

        if (Objects.nonNull(customUserDetails)) {
            log.info("JwtToken generated for user: " + user.getUsername());
            return ResponseEntity.ok(jwtUtils.generateToken(customUserDetails));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
