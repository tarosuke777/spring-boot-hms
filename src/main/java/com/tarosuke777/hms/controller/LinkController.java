package com.tarosuke777.hms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/link")
public class LinkController {

  /**
   * 外部リンクポータル画面を表示
   */
  @GetMapping("/list")
  public String list() {
    // templates/link/list.html を返却
    return "link/list";
  }
}
