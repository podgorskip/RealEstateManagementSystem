package podgorskip.managementSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final static Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DatabaseUserDetailsService databaseUserDetailsService;
    private final JwtUtils jwtUtils;
    private final ClientsRepository clientsRepository;
    private final OwnersRepository ownersRepository;
    private final RolesRepository rolesRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RequestUserDTO user) {
        Role role = rolesRepository.findByName(user.getRole());

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
                return ResponseEntity.ok("Correctly registered as an owner.");
            }
        }

       return ResponseEntity.status(204).build();
    }

    @GetMapping("/test")
    public CustomUserDetails test() {
        return databaseUserDetailsService.loadUserByUsername("podgorski.p");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody RequestUserDTO userDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

            CustomUserDetails customUserDetails = databaseUserDetailsService.loadUserByUsername(userDTO.getUsername());

            if (Objects.nonNull(customUserDetails)) {
                return ResponseEntity.ok(jwtUtils.generateToken(customUserDetails));
            }

        } catch (UsernameNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
