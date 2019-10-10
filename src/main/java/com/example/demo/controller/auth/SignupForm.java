package com.example.demo.controller.auth;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class SignupForm implements Serializable {
  private static final long serialVersionUID = 8312799286256861336L;
  @NotNull
  @Size(min = 1, max = 60)
  private String username;
  @NotNull
  @Size(min = 1)
  private String email;
  @NotNull
  @Size(min = 1, max = 30)
  private String password;
  @NotNull
  @Size(min = 1, max = 30)
  private String repassword;
  @Size(min = 0, max = 2)
  private String [] roles;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRepassword() {
    return repassword;
  }

  public void setRepassword(String repassword) {
    this.repassword = repassword;
  }

  public String[] getRoles() {
    return roles;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "SignupForm{" +
        "username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", password='******'" +
        ", repassword='******'" +
        '}';
  }

  public boolean isInvalidPassword() {
    if (password == null || repassword == null || password.length() == 0 || repassword.length() == 0) {
      return true;
    }
    return !password.equals(repassword);
  }

}
