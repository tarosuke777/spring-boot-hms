package com.tarosuke777.hms.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.form.UserForm;
import com.tarosuke777.hms.repository.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

  @Autowired private UserMapper mapper;

  @Autowired private ModelMapper modelMapper;

  @Autowired private PasswordEncoder passwordEncoder;

  @GetMapping("/signup")
  public String getSignup(Model model, @ModelAttribute UserForm form) {

    return "user/signup";
  }

  @PostMapping("/signup")
  public String signup(
      Model model,
      @ModelAttribute @Validated UserForm form,
      BindingResult bindingResult,
      @CurrentSecurityContext SecurityContext context) {

    if (bindingResult.hasErrors()) {
      return getSignup(model, form);
    }

    UserEntity user = modelMapper.map(form, UserEntity.class);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole("ROLE_GENERAL");

    mapper.insertOne(user);

    if (context.getAuthentication().isAuthenticated()) {
      return "redirect:/user/list";
    }

    return "redirect:/login";
  }

  @GetMapping("/list")
  public String getList(Model model) {

    List<UserForm> userList =
        mapper.findMany().stream().map(entity -> modelMapper.map(entity, UserForm.class)).toList();

    model.addAttribute("userList", userList);

    return "user/list";
  }

  @GetMapping("/detail/{userId}")
  public String getDetail(UserForm form, Model model, @PathVariable Integer userId) {

    UserEntity user = mapper.findOne(userId);

    form = modelMapper.map(user, UserForm.class);

    model.addAttribute("userForm", form);

    return "user/detail";
  }

  @PostMapping(value = "detail", params = "update")
  public String update(UserForm form) {

    UserEntity user = modelMapper.map(form, UserEntity.class);
    user.setPassword(passwordEncoder.encode(form.getPassword()));
    mapper.updateOne(user);

    return "redirect:/user/list";
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(UserForm form, Model model) {

    mapper.deleteOne(form.getUserId());

    return "redirect:/user/list";
  }
}
