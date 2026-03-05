package dev.shaikhmahad.security.security;

import dev.shaikhmahad.security.entities.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    @Override
    public String getUsername() {
        return user.getEmail();  // we treat email as the username
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // converts "ROLE_USER" string → Spring Security authority object
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    // These 4 control account status — return true for now,
    // but in real apps you'd check DB flags for locked/expired accounts
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return user.getEnabled(); }

    // Expose the underlying User entity when needed (e.g., to get the ID for JWT)
    public User getUser() { return user; }

}
