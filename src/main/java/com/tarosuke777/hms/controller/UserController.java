package com.tarosuke777.hms.controller;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tarosuke777.hms.domain.UserService;
import com.tarosuke777.hms.form.UserForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/signup")
  public String getSignup(@ModelAttribute UserForm form) {
    return "user/signup";
  }

  @PostMapping("/signup")
  public String signup(@ModelAttribute @Validated UserForm form, BindingResult bindingResult,
      @CurrentSecurityContext SecurityContext context) {

    if (bindingResult.hasErrors()) {
      return "user/signup";
    }

    userService.registerUser(form, "ROLE_GENERAL");

    if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
      return "redirect:/user/list";
    }

    return "redirect:/login";
  }

  @GetMapping("/list")
  public String getList(Model model) {
    model.addAttribute("userList", userService.getUserList());
    return "user/list";
  }

  @GetMapping("/detail/{userId}")
  public String getDetail(UserForm form, Model model, @PathVariable("userId") Integer userId) {
    model.addAttribute("userForm", userService.getUser(userId));
    return "user/detail";
  }

  @PostMapping(value = "detail", params = "update")
  public String update(UserForm form) {
    userService.updateUser(form);
    return "redirect:/user/list";
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(UserForm form, Model model) {
    userService.deleteUser(form.getUserId());
    return "redirect:/user/list";
  }
}
