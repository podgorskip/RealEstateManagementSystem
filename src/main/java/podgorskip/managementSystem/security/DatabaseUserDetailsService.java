package podgorskip.managementSystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import podgorskip.managementSystem.jpa.entities.User;
import podgorskip.managementSystem.jpa.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    @Autowired
    private AgentsRepository agentsRepository;
    @Autowired
    private OwnersRepository ownersRepository;
    @Autowired
    private ClientsRepository clientsRepository;
    @Autowired
    private AdministratorsRepository administratorsRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        List<User> users = new ArrayList<>();

        users.addAll(agentsRepository.findAll());
        users.addAll(ownersRepository.findAll());
        users.addAll(clientsRepository.findAll());
        users.addAll(administratorsRepository.findAll());

        User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElseThrow();

        return new CustomUserDetails(user);
    }
}
