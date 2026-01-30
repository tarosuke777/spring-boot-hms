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
import com.tarosuke777.hms.domain.ArtistService;
import com.tarosuke777.hms.form.ArtistForm;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/artist")
@RequiredArgsConstructor
public class ArtistController {

  private static final String REDIRECT_LIST = "redirect:/artist/list";
  private static final String LIST_VIEW = "artist/list";
  private static final String DETAIL_VIEW = "artist/detail";
  private static final String REGISTER_VIEW = "artist/register";

  private final ArtistService artistService;

  @GetMapping("/list")
  public String getList(Model model, @AuthenticationPrincipal UserDetails user) {
    model.addAttribute("artistList", artistService.getArtistList(user.getUsername()));
    return LIST_VIEW;
  }

  @GetMapping("/register")
  public String getRegister(@ModelAttribute ArtistForm form) {
    return REGISTER_VIEW;
  }

  @PostMapping("/register")
  public String register(@ModelAttribute @Validated ArtistForm form, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return REGISTER_VIEW;
    }

    artistService.registerArtist(form);
    return REDIRECT_LIST;
  }

  @GetMapping("/detail/{artistId}")
  public String getDetail(@PathVariable("artistId") Integer artistId, Model model,
      @AuthenticationPrincipal UserDetails user) {
    ArtistForm form = artistService.getArtist(artistId, user.getUsername());
    model.addAttribute("artistForm", form);
    return DETAIL_VIEW;
  }

  @PostMapping(value = "detail", params = "update")
  public String update(@ModelAttribute @Validated(UpdateGroup.class) ArtistForm form,
      BindingResult bindingResult, @AuthenticationPrincipal UserDetails user) {
    if (bindingResult.hasErrors()) {
      return "artist/detail";
    }

    artistService.updateArtist(form, user.getUsername());
    return "redirect:/artist/list";
  }

  @PostMapping(value = "/detail", params = "delete")
  public String delete(@Validated(DeleteGroup.class) ArtistForm form,
      @AuthenticationPrincipal UserDetails user) {
    artistService.deleteArtist(form.getArtistId(), user.getUsername());
    return "redirect:/artist/list";
  }
}
