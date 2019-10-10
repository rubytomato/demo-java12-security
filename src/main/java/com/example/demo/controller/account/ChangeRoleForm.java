package com.example.demo.controller.account;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;

public class ChangeRoleForm implements Serializable {
  private static final long serialVersionUID = -1554367575155712512L;
  @Size(min = 0, max = 2)
  private String [] roles;

  public String[] getRoles() {
    return roles;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "AuthChangeRoleForm{" +
        "roles=" + Arrays.toString(roles) +
        '}';
  }
}
