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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarosuke777.hms.entity.MovieEntity;
import com.tarosuke777.hms.form.MovieForm;
import com.tarosuke777.hms.repository.MovieMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/movie")
public class MovieController {

  @Autowired private MovieMapper movieMapper;

  @Autowired private ModelMapper modelMapper;

  @GetMapping("/list")
  public String getList(Model model) {

    List<MovieForm> movieList =
        movieMapper.findMany().stream()
            .map(entity -> modelMapper.map(entity, MovieForm.class))
            .toList();

    model.addAttribute("movieList", movieList);

    return "movie/list";
  }

  @GetMapping("/register")
  public String getRegister(MovieForm form, Model model) {

    model.addAttribute("movieForm", form);

    return "movie/register";
  }

  @PostMapping("/register")
  public String register(
      @ModelAttribute @Validated MovieForm form, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
      return getRegister(form, model);
    }

    movieMapper.insertOne(form.getMovieName(), form.getNote());

    return "redirect:/movie/list";
  }

  @GetMapping("/detail/{movieId}")
  public String getDetail(
      MovieForm form, Model model, @PathVariable("movieId") Integer movieId) {

    MovieEntity movie = movieMapper.findOne(movieId);

    form = modelMapper.map(movie, MovieForm.class);

    model.addAttribute("movieForm", form);

    return "movie/detail";
  }

  @PostMapping(value = "detail", params = "update")
  public String update(MovieForm form) {

    movieMapper.updateOne(form.getMovieId(), form.getMovieName(), form.getNote());

    return "redirect:/movie/list";
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(MovieForm form, Model model) {

    movieMapper.deleteOne(form.getMovieId());

    return "redirect:/movie/list";
  }
}
