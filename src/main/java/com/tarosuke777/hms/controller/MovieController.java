package com.tarosuke777.hms.controller;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @GetMapping("/list")
  public String getList(Model model) {
    model.addAttribute("movieList", movieService.getMovieList());
    return "movie/list";
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute MovieForm form) {
    return "movie/register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated MovieForm form, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "movie/register";
    }
    movieService.registerMovie(form);
    return "redirect:/movie/list";
  }

  @GetMapping("/detail/{movieId}")
  public String getDetail(@PathVariable("movieId") Integer movieId, Model model) {
    model.addAttribute("movieForm", movieService.getMovie(movieId));
    return "movie/detail";
  }

  @PostMapping(value = "detail", params = "update")
  public String update(@ModelAttribute @Validated MovieForm form, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "movie/detail";
    }
    movieService.updateMovie(form);
    return "redirect:/movie/list";
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(MovieForm form) {
    movieService.deleteMovie(form.getMovieId());
    return "redirect:/movie/list";
  }
}
