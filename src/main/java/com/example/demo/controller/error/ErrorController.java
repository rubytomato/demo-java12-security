package com.example.demo.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "error")
@Slf4j
public class ErrorController {

  @GetMapping(value = "denied")
  public String accessDenied(Model model) {
    model.addAttribute("ERROR_MESSAGE", "[Access denied]");
    return "error/error";
  }

  @GetMapping(value = "invalid")
  public String sessionInvalid(Model model) {
    model.addAttribute("ERROR_MESSAGE", "[Session invalid]");
    return "error/error";
  }

  @GetMapping(value = "expired")
  public String expired(Model model) {
    model.addAttribute("ERROR_MESSAGE", "[Session expired]");
    return "error/error";
  }

}
