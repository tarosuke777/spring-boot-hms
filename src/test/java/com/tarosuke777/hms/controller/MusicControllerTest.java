package com.tarosuke777.hms.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.entity.MusicEntity;
import com.tarosuke777.hms.form.MusicForm;
import com.tarosuke777.hms.repository.ArtistMapper;
import com.tarosuke777.hms.repository.MusicMapper;
import com.tarosuke777.hms.repository.TestArtistMapper;
import com.tarosuke777.hms.repository.TestMusicMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin@tarosuke777.com")
public class MusicControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private MusicMapper musicMapper;
  @Autowired private TestMusicMapper testMusicMapper;

  @Autowired private ArtistMapper artistMapper;
  @Autowired private TestArtistMapper testArtistMapper;

  @Autowired private ModelMapper modelMapper;

  private static final String LIST_ENDPOINT = "/music/list";
  private static final String LIST_VIEW = "music/list";
  private static final String LIST_URL = "/music/list";

  private static final String DETAIL_ENDPOINT = "/music/detail/{id}";
  private static final String DETAIL_VIEW = "music/detail";

  private static final String REGISTER_ENDPOINT = "/music/register";
  private static final String REGISTER_VIEW = "music/register";

  private static final String UPDATE_ENDPOINT = "/music/detail";
  private static final String DELETE_ENDPOINT = "/music/detail";

  @Test
  void getList_ShouldReturnMusicListAndArtistMap() throws Exception {

    // Given
    List<MusicForm> expectedMusicList =
        musicMapper.findMany().stream()
            .map(entity -> modelMapper.map(entity, MusicForm.class))
            .toList();

    Map<Integer, String> expectedArtistMap =
        artistMapper.findMany().stream()
            .collect(
                Collectors.toMap(
                    ArtistEntity::getArtistId,
                    ArtistEntity::getArtistName,
                    (existing, replacement) -> existing,
                    LinkedHashMap::new));

    // When & Then
    performGetListRequest()
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expectedArtistMap))
        .andExpect(model().attribute("musicList", expectedMusicList))
        .andExpect(view().name(LIST_VIEW));
  }

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  @Test
  void getDetail_ShouldReturnMusicDetailAndArtistMap() throws Exception {
    // Given
    MusicEntity musicEntity = testMusicMapper.findFirstOne();
    MusicForm expectedMusicForm =
        new MusicForm(
            musicEntity.getMusicId(),
            musicEntity.getMusicName(),
            musicEntity.getArtist().getArtistId(),
            musicEntity.getLink());
    Map<Integer, String> expectedArtistMap =
        artistMapper.findMany().stream()
            .collect(
                Collectors.toMap(
                    ArtistEntity::getArtistId,
                    ArtistEntity::getArtistName,
                    (existing, replacement) -> existing,
                    LinkedHashMap::new));

    // When & Then
    performGetDetailRequest(musicEntity.getMusicId())
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expectedArtistMap))
        .andExpect(model().attribute("musicForm", expectedMusicForm))
        .andExpect(view().name(DETAIL_VIEW))
        .andExpect(model().hasNoErrors());
  }

  private ResultActions performGetDetailRequest(int musicId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, musicId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  @Test
  void getRegister_ShouldReturnRegisterPageWithArtistMap() throws Exception {

    // Given
    Map<Integer, String> expectedArtistMap =
        artistMapper.findMany().stream()
            .collect(
                Collectors.toMap(
                    ArtistEntity::getArtistId,
                    ArtistEntity::getArtistName,
                    (existing, replacement) -> existing,
                    LinkedHashMap::new));

    // When & Then
    performGetRegisterRequest()
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expectedArtistMap))
        .andExpect(view().name(REGISTER_VIEW))
        .andExpect(model().hasNoErrors());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  @Test
  void register_WithValidData_ShouldRedirectToList() throws Exception {

    // Given
    ArtistEntity artistEntity = testArtistMapper.findFirstOne();
    MusicForm musicForm = new MusicForm(null, "test", artistEntity.getArtistId(),null);

    // When & Then
    performRegisterRequest(musicForm)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    MusicEntity musicEntity = testMusicMapper.findLastOne();

    Assertions.assertEquals(musicForm.getMusicName(), musicEntity.getMusicName());
    Assertions.assertEquals(musicForm.getArtistId(), musicEntity.getArtist().getArtistId());
  }

  private ResultActions performRegisterRequest(MusicForm form) throws Exception {
    return mockMvc
        .perform(
            post(REGISTER_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("musicName", form.getMusicName())
                .param("artistId", String.valueOf(form.getArtistId())))
        .andDo(print());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    MusicEntity expectedMusic = testMusicMapper.findFirstOne();
    expectedMusic.setMusicName("好きになってはいけない理由2");

    // When & Then
    performUpdateRequest(expectedMusic)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    MusicEntity music = testMusicMapper.findFirstOne();
    assertEquals(music, expectedMusic);
  }

  private ResultActions performUpdateRequest(MusicEntity music) throws Exception {
    return mockMvc
        .perform(
            post(UPDATE_ENDPOINT)
                .with(csrf())
                .param("update", "")
                .param("musicId", music.getMusicId().toString())
                .param("musicName", music.getMusicName())
                .param("artistId", music.getArtist().getArtistId().toString()))
        .andDo(print());
  }

  @Test
  void delete_ExistingMusic_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    MusicEntity targetMusic = musicMapper.findOne(1);

    // When & Then
    performDeleteRequest(targetMusic.getMusicId())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    MusicEntity music = musicMapper.findOne(1);
    assertNull(music);
  }

  private ResultActions performDeleteRequest(int musicId) throws Exception {
    return mockMvc
        .perform(
            post(DELETE_ENDPOINT)
                .with(csrf())
                .param("delete", "")
                .param("musicId", String.valueOf(musicId)))
        .andDo(print());
  }
}
