package com.example.demo.auth;

import java.util.Arrays;

public enum UserRoles {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN"),
  ;

  private String value;

  public String getValue() {
    return value;
  }

  UserRoles(String value) {
    this.value = value;
  }

  public static UserRoles lookup(String value) {
    return Arrays.stream(values())
        .filter(role -> role.getValue().equals(value))
        .findFirst().get();
  }
}
