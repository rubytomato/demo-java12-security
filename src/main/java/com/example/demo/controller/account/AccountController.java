package com.example.demo.controller.account;

import com.example.demo.auth.SimpleLoginUser;
import com.example.demo.auth.UserRolesUtil;
import com.example.demo.model.User;
import com.example.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;

@Controller
@RequestMapping(value = "account")
@Slf4j
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping(value = "change/password")
  public String changePasswordForm(Model model) {
    log.debug("change password form");
    if (!model.containsAttribute("changePasswordForm")) {
      log.debug("new change password form");
      ChangePasswordForm changePasswordForm = new ChangePasswordForm();
      model.addAttribute("changePasswordForm", changePasswordForm);
    }
    return "account/change_password_form";
  }

  @PostMapping(value = "change/password")
  public String changePassword(@AuthenticationPrincipal SimpleLoginUser loggedinUser,
                               @Validated ChangePasswordForm changePasswordForm, BindingResult result, Model model) {
    log.debug("change password user:{}, form:{}", loggedinUser.getUser(), changePasswordForm);
    if (result.hasErrors()) {
      log.error("change password validation error");
      return changePasswordForm(model);
    }
    if (changePasswordForm.isInvalidNewPassword()) {
      log.error("re new password");
      model.addAttribute("FORM_ERROR", "change password re password error");
      return changePasswordForm(model);
    }
    accountService.changePassword(
        loggedinUser.getUser(),
        changePasswordForm.getNewPassword());
    return "redirect:/";
  }

  @GetMapping(value = "change/role")
  public String changeRoleForm(@AuthenticationPrincipal SimpleLoginUser loggedinUser, Model model) {
    if (!model.containsAttribute("changeRoleForm")) {
      log.debug("new change role form");
      ChangeRoleForm changeRoleForm = new ChangeRoleForm();
      String[] roles = UserRolesUtil.toArray(loggedinUser.getAuthorities());
      changeRoleForm.setRoles(roles);
      model.addAttribute("changeRoleForm", changeRoleForm);
    }
    return "account/change_role_form";
  }

  @PostMapping(value = "change/role")
  public String changeRole(@AuthenticationPrincipal SimpleLoginUser loggedinUser,
                           @Validated ChangeRoleForm changeRoleForm, BindingResult result, Model model) {
    if (result.hasErrors()) {
      log.error("change role validation error");
      return changeRoleForm(loggedinUser, model);
    }
    accountService.changeRole(loggedinUser.getUser(), changeRoleForm.getRoles());
    return "redirect:/";
  }

  @GetMapping(value = "change/profile")
  public String changeProfileForm(@AuthenticationPrincipal SimpleLoginUser loggedinUser, Model model) {
    log.debug("change profile form");
    if (!model.containsAttribute("changeProfileForm")) {
      log.debug("new change profile form");
      ChangeProfileForm changeProfileForm = new ChangeProfileForm();
      changeProfileForm.setNickName(loggedinUser.getUser().getUserProfile().getNickName());
      model.addAttribute("changeProfileForm", changeProfileForm);
    }
    return "account/change_profile_form";
  }

  @PostMapping(value = "change/profile")
  public String changeProfile(@AuthenticationPrincipal SimpleLoginUser loggedinUser,
                              @Validated ChangeProfileForm changeProfileForm, BindingResult result, Model model) {
    log.debug("change profile user:{}, form:{}", loggedinUser.getUser(), changeProfileForm);
    if (result.hasErrors()) {
      log.error("change profile validation error");
      return changeProfileForm(loggedinUser, model);
    }
    accountService.changeProfile(
        loggedinUser.getUser(),
        changeProfileForm.getNickName(),
        checkUploadFile(changeProfileForm.getAvatarImage()),
        changeProfileForm.getDeleteAvatarImage());
    return "redirect:/";
  }

  @GetMapping(value = "delete")
  public String deleteForm(Model model) {
    log.info("delete form");
    if (!model.containsAttribute("deleteForm")) {
      DeleteForm deleteForm = new DeleteForm();
      model.addAttribute("deleteForm", deleteForm);
    }
    return "account/delete_form";
  }

  @PostMapping(value = "delete")
  public String delete(@AuthenticationPrincipal SimpleLoginUser loggedinUser, HttpServletRequest request,
                       DeleteForm deleteForm) {
    log.info("delete form user:{}", loggedinUser.getUser());
    accountService.delete(loggedinUser.getUser());
    try {
      request.logout();
    } catch (ServletException e) {
      throw new RuntimeException("delete and logout failure", e);
    }
    return "redirect:/";
  }

  @GetMapping(value = "avatar", produces = MediaType.IMAGE_JPEG_VALUE)
  public @ResponseBody byte[] avatarImage(@AuthenticationPrincipal(expression = "user") User user) {
    log.info("avatar image : {}", user);
    return user.getUserProfile().getAvatarImage();
  }

  private byte[] checkUploadFile(MultipartFile uploadFile) {
    if (uploadFile == null || uploadFile.isEmpty()) {
      log.info("upload file size empty");
      return new byte[0];
    }
    try {
      log.info("upload file name : {}, file size : {}", uploadFile.getOriginalFilename(), uploadFile.getSize());
      return uploadFile.getBytes();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
