package com.example.demo.auth;

import com.example.demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class SimpleLoginUser implements UserDetails, CredentialsContainer {
  private static final long serialVersionUID = -888887602572409628L;

  private final String username;
  private String password;
  private final Set<GrantedAuthority> authorities;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;
  private final User user;

  public SimpleLoginUser(User user) {
    if ((Objects.isNull(user.getEmail()) || "".equals(user.getEmail())) ||
        (Objects.isNull(user.getPassword()) || "".equals(user.getPassword()))) {
      throw new IllegalArgumentException(
          "Cannot pass null or empty values to constructor");
    }
    this.username = user.getEmail();
    this.password = user.getPassword();
    this.authorities = UserRolesUtil.toSet(user.getRoles());
    this.accountNonExpired = true;
    this.accountNonLocked = !user.getLockFlag();
    this.credentialsNonExpired = true;
    this.enabled = !user.getDisableFlag();
    this.user = user;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void eraseCredentials() {
    this.password = null;
  }

  public User getUser() {
    return user;
  }

  @Override
  public boolean equals(Object rhs) {
    if (!(rhs instanceof SimpleLoginUser)) {
      return false;
    }
    return this.username.equals(((SimpleLoginUser)rhs).username);
  }

  @Override
  public int hashCode() {
    return this.username.hashCode();
  }

  @Override
  public String toString() {
    return "SimpleLoginUser{" +
        "username='" + username + '\'' +
        ", password='******'" +
        ", authorities=" + authorities +
        ", accountNonExpired=" + isAccountNonExpired() +
        ", accountNonLocked=" + isCredentialsNonExpired() +
        ", credentialsNonExpired=" + isCredentialsNonExpired() +
        ", enabled=" + isEnabled() +
        ", user=" + user +
        '}';
  }

}
