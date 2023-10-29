package podgorskip.managementSystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import podgorskip.managementSystem.jpa.entities.Client;
import podgorskip.managementSystem.jpa.entities.Owner;
import podgorskip.managementSystem.jpa.entities.Role;
import podgorskip.managementSystem.jpa.repositories.ClientsRepository;
import podgorskip.managementSystem.jpa.repositories.OwnersRepository;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.dto.UserDTO;
import podgorskip.managementSystem.jpa.entities.User;
import podgorskip.managementSystem.security.JwtUtils;
import podgorskip.managementSystem.security.DatabaseUserDetailsService;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DatabaseUserDetailsService databaseUserDetailsService;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private OwnersRepository ownersRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {

        switch (user.getRole()) {
            case CLIENT -> {
                Client client = new Client();
                client.setFirstName(user.getFirstName());
                client.setLastName(user.getLastName());
                client.setUsername(user.getUsername());
                client.setPassword(passwordEncoder.encode(user.getPassword()));

                clientsRepository.save(client);
                return ResponseEntity.ok("Correctly registered as a client.");
            }

            case OWNER -> {
                Owner owner = new Owner();
                owner.setFirstName(user.getFirstName());
                owner.setLastName(user.getLastName());
                owner.setUsername(user.getUsername());
                owner.setPassword(passwordEncoder.encode(user.getPassword()));

                ownersRepository.save(owner);
                return ResponseEntity.ok("Correctly registered as an owner.");
            }
        }

       return ResponseEntity.status(204).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserDTO userDTO) {
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
