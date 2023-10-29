package podgorskip.managementSystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import podgorskip.managementSystem.jpa.entities.Accountant;
import podgorskip.managementSystem.jpa.entities.Agent;
import podgorskip.managementSystem.jpa.entities.User;
import podgorskip.managementSystem.jpa.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    @Autowired
    private AccountantsRepository accountantsRepository;
    @Autowired
    private AgentsRepository agentsRepository;
    @Autowired
    private BrokersRepository brokersRepository;
    @Autowired
    private OwnersRepository ownersRepository;
    @Autowired
    private ClientsRepository clientsRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        List<User> users = new ArrayList<>();

        users.addAll(accountantsRepository.findAll());
        users.addAll(agentsRepository.findAll());
        users.addAll(ownersRepository.findAll());
        users.addAll(clientsRepository.findAll());

        User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElseThrow();

        return new CustomUserDetails(user);
    }
}
