package com.example.demo.controller.account;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class ChangePasswordForm implements Serializable {
  private static final long serialVersionUID = 3766183742850116638L;
  @NotNull
  @Size(min = 1, max = 30)
  private String newPassword;
  @NotNull
  @Size(min = 1, max = 30)
  private String reNewPassword;

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getReNewPassword() {
    return reNewPassword;
  }

  public void setReNewPassword(String reNewPassword) {
    this.reNewPassword = reNewPassword;
  }

  @Override
  public String toString() {
    return "ChangePasswordForm{" +
        ", newPassword='******'" +
        ", reNewPassword='******'" +
        '}';
  }

  public boolean isInvalidNewPassword() {
    if (newPassword == null || reNewPassword == null || newPassword.length() == 0 || reNewPassword.length() == 0) {
      return true;
    }
    return !newPassword.equals(reNewPassword);
  }

}
