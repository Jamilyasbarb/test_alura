package br.com.alura.AluraFake.security;

import br.com.alura.AluraFake.domain.User;
import br.com.alura.AluraFake.domain.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;
    private final Long id;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    public boolean hasRole(Role role) {
        return authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_" + role.name()));
    }

    public Role getRole() {
        return authorities.stream().findFirst().map(grantedAuthority -> {
            String roleName = grantedAuthority.getAuthority().replace("ROLE_", "");
            return Role.toEnumFromName(roleName);
        }).orElse(null);
    }
}
