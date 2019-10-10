package com.example.demo.controller.contents;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "memo")
@Slf4j
public class MemoController {

  @GetMapping
  public String index() {
    return "contents/memo/index";
  }

  @GetMapping(value = "list")
  public String list() {
    return "contents/memo/list";
  }

}
