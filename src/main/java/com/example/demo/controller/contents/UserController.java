package com.example.demo.controller.contents;

import com.example.demo.auth.SimpleLoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "user")
@Slf4j
public class UserController {

  @GetMapping
  public String index(@AuthenticationPrincipal SimpleLoginUser user, Model model) {
    log.debug("user index. user:{}", user.getUser().getName());
    return "contents/user/index";
  }

  @GetMapping(value = "list")
  public String list(@AuthenticationPrincipal SimpleLoginUser user, Model model) {
    log.debug("user list. user:{}", user.getUser().getName());
    return "contents/user/list";
  }

}
