package vn.triumphstudio.clothesshop.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.domain.enumration.Role;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private final UserEntity user;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(UserEntity user) {
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public long getId() {
        return this.user.getId();
    }

    public Role getRole() {
        return this.user.getRole();
    }

    public String getName() {
        return this.user.getName();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
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
        return user.isEnabled();
    }
}
