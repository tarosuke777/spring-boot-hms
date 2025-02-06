package com.tarosuke777.hms.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarosuke777.hms.form.AuthorForm;
import com.tarosuke777.hms.repository.AuthorMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/author")
public class AuthorController {

  @Autowired private AuthorMapper authorMapper;
  
  @Autowired private ModelMapper modelMapper;

  @GetMapping("/list")
  public String getList(Model model) {

    List<AuthorForm> authorList =
    	authorMapper.findMany().stream()
            .map(entity -> modelMapper.map(entity, AuthorForm.class))
            .toList();

    model.addAttribute("authorList", authorList);

    return "author/list";
  }
  
  @GetMapping("/register")
  public String getRegister(AuthorForm form, Model model) {

    model.addAttribute("authorForm", form);

    return "author/register";
  }

  @PostMapping("/register")
  public String register(
      @ModelAttribute @Validated AuthorForm form, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
      return getRegister(form, model);
    }

    authorMapper.insertOne(form.getAuthorName());

    return "redirect:/author/list";
  }

}
