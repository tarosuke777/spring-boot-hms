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
import com.tarosuke777.hms.domain.MovieService;
import com.tarosuke777.hms.form.MovieForm;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

  private static final String REDIRECT_LIST = "redirect:/movie/list";
  private static final String LIST_VIEW = "movie/list";
  private static final String DETAIL_VIEW = "movie/detail";
  private static final String REGISTER_VIEW = "movie/register";

  private final MovieService movieService;

  @GetMapping("/list")
  public String getList(Model model, @AuthenticationPrincipal UserDetails user) {
    model.addAttribute("movieList", movieService.getMovieList(user.getUsername()));
    return LIST_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute MovieForm form) {
    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated MovieForm form, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return REGISTER_VIEW;
    }
    movieService.registerMovie(form);
    return REDIRECT_LIST;
  }

  @GetMapping("/detail/{movieId}")
  public String getDetail(@PathVariable("movieId") Integer movieId, Model model,
      @AuthenticationPrincipal UserDetails user) {
    model.addAttribute("movieForm", movieService.getMovie(movieId, user.getUsername()));
    return DETAIL_VIEW;
  }

  @PostMapping(value = "detail", params = "update")
  public String update(@ModelAttribute @Validated(UpdateGroup.class) MovieForm form,
      BindingResult bindingResult, @AuthenticationPrincipal UserDetails user) {
    if (bindingResult.hasErrors()) {
      return DETAIL_VIEW;
    }
    movieService.updateMovie(form, user.getUsername());
    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(@Validated(DeleteGroup.class) MovieForm form,
      @AuthenticationPrincipal UserDetails user) {
    movieService.deleteMovie(form.getMovieId(), user.getUsername());
    return REDIRECT_LIST;
  }
}
