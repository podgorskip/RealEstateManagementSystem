package podgorskip.managementSystem.controllers;

import io.micrometer.common.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import podgorskip.managementSystem.security.CustomUserDetails;
import podgorskip.managementSystem.dto.UserDTO;
import podgorskip.managementSystem.jpa.entities.User;
import podgorskip.managementSystem.jpa.repositories.UsersRepository;
import podgorskip.managementSystem.security.JwtUtils;
import podgorskip.managementSystem.security.DatabaseUserDetailsService;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DatabaseUserDetailsService databaseUserDetailsService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (Objects.nonNull(user)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            usersRepository.save(user);

            return ResponseEntity.ok("Successfully registered.");
        }

        return ResponseEntity.status(400).build();
    }

    @GetMapping("/test")
    public UserDetails test() {
        return databaseUserDetailsService.loadUserByUsername("podgorski.p");
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
