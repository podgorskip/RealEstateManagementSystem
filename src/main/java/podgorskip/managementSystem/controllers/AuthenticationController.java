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
import podgorskip.managementSystem.jpa.entities.User;
import podgorskip.managementSystem.jpa.repositories.ClientsRepository;
import podgorskip.managementSystem.jpa.repositories.OwnersRepository;
import podgorskip.managementSystem.jpa.repositories.RolesRepository;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.security.JwtUtils;
import podgorskip.managementSystem.security.DatabaseUserDetailsService;
import podgorskip.managementSystem.utils.Roles;
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

    @PostMapping("/register-client")
    public ResponseEntity<String> registerClient(@RequestBody RequestUserDTO user) {

        if (!user.validateData()) {
            String message = "Some of the expected criteria were not met for the provided credentials";
            log.info(message);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(message);
        }

        clientsRepository.save((Client) createUser(user, Roles.CLIENT));

        log.info("Correctly created a new client account.");
        log.info("Clients database updated.");

        return ResponseEntity.ok("Correctly registered as a client.");
    }

    @PostMapping("/register-owner")
    public ResponseEntity<String> registerOwner(@RequestBody RequestUserDTO user) {

        if (!user.validateData()) {
            String message = "Some of the expected criteria were not met for the provided credentials";
            log.info(message);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(message);
        }

        ownersRepository.save((Owner) createUser(user, Roles.OWNER));

        log.info("Correctly created a new owner account.");
        log.info("Owners database updated.");

        return ResponseEntity.ok("Correctly registered as an owner.");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationDTO user) {
        log.info("Authentication requested for user: " + user.getUsername());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        CustomUserDetails customUserDetails = databaseUserDetailsService.loadUserByUsername(user.getUsername());

        if (Objects.nonNull(customUserDetails)) {
            log.info("Authentication completed successfully");
            log.info("JwtToken generated for user: " + user.getUsername());

            return ResponseEntity.ok(jwtUtils.generateToken(customUserDetails));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private User createUser(RequestUserDTO userDTO, Roles role) {
        User user;

        if (Roles.CLIENT.name().equals(role.name())) user = new Client();
        else user = new Owner();

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreated(new Date());
        user.setRole(rolesRepository.findByName("ROLE_" + role.name()));
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return user;
    }
}
