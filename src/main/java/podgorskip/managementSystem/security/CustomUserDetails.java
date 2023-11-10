package podgorskip.managementSystem.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import podgorskip.managementSystem.jpa.entities.Privilege;
import podgorskip.managementSystem.jpa.entities.Role;
import podgorskip.managementSystem.jpa.entities.User;
import podgorskip.managementSystem.utils.Roles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(getPrivileges(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Roles getRole() {
        return Roles.valueOf(user.getRole().getName().substring(5));
    }

    private List<String> getPrivileges(Role role) {

        List<String> privileges = new ArrayList<>();

        for (Privilege privilege : role.getPrivileges()) {
            privileges.add(privilege.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }

        return authorities;
    }

}
