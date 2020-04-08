package vn.com.buaansach.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.com.buaansach.model.entity.UserEntity;
import vn.com.buaansach.exception.UserNotActivatedException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean activated;

    public UserPrincipal(Long id, String username, String password, boolean activated, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.activated = activated;
    }

    public static UserPrincipal create(String lowercaseLogin, UserEntity userEntity) {
        if (!userEntity.isActivated()) {
            throw new UserNotActivatedException("Tài khoản " + lowercaseLogin + " đã bị khóa");
        }

        List<GrantedAuthority> authorities = userEntity.getAuthorities().stream().map(authority ->
                new SimpleGrantedAuthority(authority.getName())
        ).collect(Collectors.toList());

        return new UserPrincipal(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.isActivated(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public boolean isActivated() {
        return activated;
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
        return activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
