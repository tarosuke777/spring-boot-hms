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
import com.tarosuke777.hms.exception.IllegalRequestException;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

  private static final String REDIRECT_LIST = "redirect:/author/list";
  private static final String LIST_VIEW = "author/list";
  private static final String DETAIL_VIEW = "author/detail";
  private static final String REGISTER_VIEW = "author/register";

  private final AuthorService authorService;

  @GetMapping("/list")
  public String getList(Model model, @AuthenticationPrincipal UserDetails user) {
    model.addAttribute("authorList", authorService.getAuthorList(user.getUsername()));
    return LIST_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute AuthorForm form) {
    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated AuthorForm form, BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return REGISTER_VIEW;
    }
    authorService.registerAuthor(form);
    return REDIRECT_LIST;
  }

  @GetMapping("/detail/{authorId}")
  public String getDetail(@PathVariable("authorId") Integer authorId, Model model,
      @AuthenticationPrincipal UserDetails user) {
    AuthorForm form = authorService.getAuthor(authorId, user.getUsername());
    model.addAttribute("authorForm", form);
    return DETAIL_VIEW;
  }

  @PostMapping(value = "detail", params = "update")
  public String update(@ModelAttribute @Validated(UpdateGroup.class) AuthorForm form,
      BindingResult bindingResult, @AuthenticationPrincipal UserDetails user) {

    // id や version にエラーがある場合は、改ざんとみなしてシステムエラー
    if (bindingResult.hasFieldErrors(AuthorForm.Fields.id)
        || bindingResult.hasFieldErrors(AuthorForm.Fields.version)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }

    if (bindingResult.hasErrors()) {
      return DETAIL_VIEW;
    }
    authorService.updateAuthor(form, user.getUsername());
    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(@Validated(DeleteGroup.class) AuthorForm form, BindingResult bindingResult,
      @AuthenticationPrincipal UserDetails user) {

    // id にエラーがある場合は改ざんとみなしてシステムエラー
    if (bindingResult.hasFieldErrors(AuthorForm.Fields.id)) {
      throw new IllegalRequestException("不正なリクエストを検出しました（改ざんの疑い）");
    }

    authorService.deleteAuthor(form.getId(), user.getUsername());
    return REDIRECT_LIST;
  }

}
