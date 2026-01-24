package com.tarosuke777.hms.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tarosuke777.hms.domain.AuthorService;
import com.tarosuke777.hms.form.AuthorForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

  private final AuthorService authorService;

  @GetMapping("/list")
  public String getList(Model model, @AuthenticationPrincipal UserDetails user) {
    model.addAttribute("authorList", authorService.getAuthorList(user.getUsername()));
    return "author/list";
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute AuthorForm form) {
    return "author/register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated AuthorForm form, BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return "author/register";
    }
    authorService.registerAuthor(form);
    return "redirect:/author/list";
  }

  @GetMapping("/detail/{authorId}")
  public String getDetail(@PathVariable("authorId") Integer authorId, Model model,
      @AuthenticationPrincipal UserDetails user) {
    AuthorForm form = authorService.getAuthor(authorId, user.getUsername());
    model.addAttribute("authorForm", form);
    return "author/detail";
  }

  @PostMapping(value = "detail", params = "update")
  public String update(@ModelAttribute @Validated AuthorForm form, BindingResult bindingResult,
      @AuthenticationPrincipal UserDetails user) {
    if (bindingResult.hasErrors()) {
      return "author/detail";
    }
    authorService.updateAuthor(form, user.getUsername());
    return "redirect:/author/list";
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(AuthorForm form, @AuthenticationPrincipal UserDetails user) {
    authorService.deleteAuthor(form.getAuthorId(), user.getUsername());
    return "redirect:/author/list";
  }

}
