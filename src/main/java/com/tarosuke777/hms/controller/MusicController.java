package com.tarosuke777.hms.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tarosuke777.hms.domain.ArtistService;
import com.tarosuke777.hms.domain.MusicService;
import com.tarosuke777.hms.form.MusicForm;

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

  private final MusicService musicService;
  private final ArtistService artistService;

  @GetMapping("/list")
  public String getList(Model model) {

    List<MusicForm> musicList = musicService.getMusicList();
    Map<Integer, String> artistMap = artistService.getArtistMap();

    addAttributesToModel(model, musicList, artistMap);

    return LIST_VIEW;
  }

  @GetMapping("/detail/{musicId}")
  public String getDetail(@PathVariable("musicId") Integer musicId, Model model) {

    MusicForm musicForm = musicService.getMusicDetails(musicId);
    Map<Integer, String> artistMap = artistService.getArtistMap();

    addAttributesToModel(model, musicForm, artistMap);

    return DETAIL_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(MusicForm musicForm, Model model) {

    Map<Integer, String> artistMap = artistService.getArtistMap();

    addAttributesToModel(model, artistMap);

    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated MusicForm form, BindingResult bindingResult,
      Model model) {

    if (bindingResult.hasErrors()) {
      return getRegister(form, model);
    }

    musicService.registerMusic(form);

    return REDIRECT_LIST;
  }

  @PostMapping(value = "detail", params = "update")
  public String update(MusicForm form) {

    musicService.updateMusic(form);

    return REDIRECT_LIST;
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(MusicForm form, Model model) {

    musicService.deleteMusic(form.getMusicId());

    return REDIRECT_LIST;
  }

  private void addAttributesToModel(Model model, List<MusicForm> musicList,
      Map<Integer, String> artistMap) {
    model.addAttribute("artistMap", artistMap);
    model.addAttribute("musicList", musicList);
  }

  private void addAttributesToModel(Model model, MusicForm musicForm,
      Map<Integer, String> artistMap) {
    model.addAttribute("musicForm", musicForm);
    model.addAttribute("artistMap", artistMap);
  }

  private void addAttributesToModel(Model model, Map<Integer, String> artistMap) {
    model.addAttribute("artistMap", artistMap);
  }
}
