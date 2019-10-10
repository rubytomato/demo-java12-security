package com.example.demo.controller.contents;

import com.example.demo.auth.SimpleLoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "admin")
@Slf4j
public class AdminController {

  @GetMapping
  public String index(@AuthenticationPrincipal SimpleLoginUser user, Model model) {
    log.debug("admin index. user:{}", user.getUser().getName());
    return "contents/admin/index";
  }

  @GetMapping(value = "list")
  public String list(@AuthenticationPrincipal SimpleLoginUser user, Model model) {
    log.debug("admin list. user:{}", user.getUser().getName());
    return "contents/admin/list";
  }

}
