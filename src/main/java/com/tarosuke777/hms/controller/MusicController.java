package com.tarosuke777.hms.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.tarosuke777.hms.controller.form.MusicForm;
import com.tarosuke777.hms.repository.ArtistMapper;
import com.tarosuke777.hms.repository.MusicMapper;
import com.tarosuke777.hms.repository.entity.ArtistEntity;
import com.tarosuke777.hms.repository.entity.MusicEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {

  private static final String REDIRECT_LIST = "redirect:/music/list";
  private static final String LIST_VIEW = "music/list";
  private static final String DETAIL_VIEW = "music/detail";
  private static final String REGISTER_VIEW = "music/register";

  private final ArtistMapper artistMapper;
  private final MusicMapper musicMapper;

  private final ModelMapper modelMapper;

  @GetMapping("/list")
  public String getList(Model model) {

    List<MusicForm> musicList =
        musicMapper.findMany().stream()
            .map(entity -> modelMapper.map(entity, MusicForm.class))
            .toList();

    List<ArtistEntity> artist = artistMapper.findMany();

    Map<Integer, String> artistMap =
        artist.stream()
            .collect(
                Collectors.toMap(
                    ArtistEntity::getArtistId,
                    ArtistEntity::getArtistName,
                    (e1, e2) -> e1,
                    LinkedHashMap::new));

    model.addAttribute("artistMap", artistMap);
    model.addAttribute("musicList", musicList);

    return LIST_VIEW;
  }

  @GetMapping("/detail/{musicId}")
  public String getDetail(MusicForm form, Model model, @PathVariable("musicId") Integer musicId) {

    MusicEntity music = musicMapper.findOne(musicId);
    List<ArtistEntity> artist = artistMapper.findMany();

    Map<Integer, String> artistMap =
        artist.stream()
            .collect(
                Collectors.toMap(
                    ArtistEntity::getArtistId,
                    ArtistEntity::getArtistName,
                    (e1, e2) -> e1,
                    LinkedHashMap::new));

    form = modelMapper.map(music, MusicForm.class);
    form.setArtistId(music.getArtist().getArtistId());

    model.addAttribute("musicForm", form);
    model.addAttribute("artistMap", artistMap);

    return DETAIL_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(MusicForm form, Model model) {

    List<ArtistEntity> artist = artistMapper.findMany();

    Map<Integer, String> artistMap =
        artist.stream()
            .collect(
                Collectors.toMap(
                    ArtistEntity::getArtistId,
                    ArtistEntity::getArtistName,
                    (e1, e2) -> e1,
                    LinkedHashMap::new));

    model.addAttribute("musicForm", form);
    model.addAttribute("artistMap", artistMap);

    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(
      @ModelAttribute @Validated MusicForm form, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
      return getRegister(form, model);
    }

    musicMapper.insertOne(form.getMusicName(), form.getArtistId());

    return REDIRECT_LIST;
  }

  @PostMapping(value = "detail", params = "update")
  public String update(MusicForm form) {

    musicMapper.updateOne(form.getMusicId(), form.getMusicName(), form.getArtistId());

    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(MusicForm form, Model model) {

    musicMapper.deleteOne(form.getMusicId());

    return REDIRECT_LIST;
  }
}
