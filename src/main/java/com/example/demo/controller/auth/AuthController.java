package com.example.demo.controller.auth;

import com.example.demo.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@Slf4j
public class AuthController {
  private final AccountService accountService;

  public AuthController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping(value = "signup")
  public String signupForm(Model model) {
    log.debug("signup form");
    if (!model.containsAttribute("signupForm")) {
      SignupForm signupForm = new SignupForm();
      model.addAttribute("signupForm", signupForm);
    }
    return "auth/signup_form";
  }

  @PostMapping(value = "signup")
  public String signup(@Validated SignupForm signupForm, BindingResult result, Model model) {
    log.debug("signup:{}", signupForm);
    if (result.hasErrors()) {
      log.error("signup validation error");
      return signupForm(model);
    }
    if (signupForm.isInvalidPassword()) {
      model.addAttribute("FORM_ERROR", "パスワードが一致していません");
      return signupForm(model);
    }
    if (accountService.exists(signupForm.getEmail())) {
      model.addAttribute("FORM_ERROR", "メールアドレスは登録できません");
      return signupForm(model);
    }
    accountService.register(
        signupForm.getUsername(),
        signupForm.getEmail(),
        signupForm.getPassword(),
        signupForm.getRoles());
    return "redirect:/";
  }

  @GetMapping(value = "signin")
  public String signinForm(HttpServletRequest req) {
    log.debug("signin form");
    HttpSession session = req.getSession(false);
    if (session != null) {
      SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
      if (savedRequest != null) {
        log.debug("redirectUrl:{}", savedRequest.getRedirectUrl());
        savedRequest.getParameterMap().forEach((k,v) -> {
          log.debug("key:{} value:{}", k, v);
        });
      }
    }
    return "auth/signin_form";
  }

  @GetMapping(value = "signout")
  public String signoutForm() {
    log.debug("signout form");
    return "auth/signout_form";
  }

}
