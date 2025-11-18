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

import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.form.ArtistForm;
import com.tarosuke777.hms.repository.ArtistMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/artist")
public class ArtistController {

  @Autowired
  private ArtistMapper artistMapper;

  @Autowired
  private ModelMapper modelMapper;

  @GetMapping("/list")
  public String getList(Model model) {

    List<ArtistForm> artistList = artistMapper.findMany().stream()
        .map(entity -> modelMapper.map(entity, ArtistForm.class)).toList();

    model.addAttribute("artistList", artistList);

    return "artist/list";
  }

  @GetMapping("/register")
  public String getRegister(ArtistForm form, Model model) {

    model.addAttribute("artistForm", form);

    return "artist/register";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated ArtistForm form, BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      return getRegister(form, model);
    }

    artistMapper.insertOne(form.getArtistName());

    return "redirect:/artist/list";
  }

  @GetMapping("/detail/{artistId}")
  public String getDetail(ArtistForm form, Model model,
      @PathVariable("artistId") Integer artistId) {

    ArtistEntity artist = artistMapper.findOne(artistId);

    form = modelMapper.map(artist, ArtistForm.class);

    model.addAttribute("artistForm", form);

    return "artist/detail";
  }

  @PostMapping(value = "detail", params = "update")
  public String update(ArtistForm form) {

    artistMapper.updateOne(form.getArtistId(), form.getArtistName());

    return "redirect:/artist/list";
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(ArtistForm form, Model model) {

    artistMapper.deleteOne(form.getArtistId());

    return "redirect:/artist/list";
  }
}
