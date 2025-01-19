package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.form.ArtistForm;
import com.tarosuke777.hms.repository.ArtistMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@WithUserDetails("admin@tarosuke777.com")
public class ArtistControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ArtistMapper artistMapper;

  private static final String LIST_ENDPOINT = "/artist/list";
  private static final String LIST_VIEW = "artist/list";
  private static final String LIST_URL = "/artist/list";

  private static final String DETAIL_ENDPOINT = "/artist/detail/{id}";
  private static final String DETAIL_VIEW = "artist/detail";

  private static final String REGISTER_ENDPOINT = "/artist/register";
  private static final String REGISTER_VIEW = "artist/register";

  private static final String UPDATE_ENDPOINT = "/artist/detail";
  private static final String DELETE_ENDPOINT = "/artist/detail";

  @Test
  void getList_ShouldReturnArtistList() throws Exception {

    // Given
    List<ArtistForm> expectedArtistList =
        Arrays.asList(new ArtistForm(1, "藤川千愛"), new ArtistForm(2, "分島花音"));

    // When & Then
    performGetListRequest()
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistList", expectedArtistList))
        .andExpect(view().name(LIST_VIEW));
  }

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  @Test
  void getDetail_ShouldReturnArtistDetail() throws Exception {
    // Given
    final Integer targetArtistId = 1;
    ArtistForm expectedArtistForm = new ArtistForm(targetArtistId, "藤川千愛");

    // When & Then
    performGetDetailRequest(targetArtistId)
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistForm", expectedArtistForm))
        .andExpect(view().name(DETAIL_VIEW))
        .andExpect(model().hasNoErrors());
  }

  private ResultActions performGetDetailRequest(int artistId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, artistId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  @Test
  void getRegister_ShouldReturnRegisterPage() throws Exception {

    // Given

    // When & Then
    performGetRegisterRequest()
        .andExpect(status().isOk())
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
    String artistName = "TestArtistName";
    List<ArtistEntity> expectedArtistList =
        Arrays.asList(
            new ArtistEntity(1, "藤川千愛"),
            new ArtistEntity(2, "分島花音"),
            new ArtistEntity(3, "TestArtistName"));

    // When & Then
    performRegisterRequest(artistName)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    List<ArtistEntity> artistList = artistMapper.findMany();
    Assertions.assertIterableEquals(expectedArtistList, artistList);
  }

  private ResultActions performRegisterRequest(String artistName) throws Exception {
    return mockMvc
        .perform(
            post(REGISTER_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("artistName", artistName))
        .andDo(print());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    ArtistEntity expectedArtist = artistMapper.findOne(1);
    expectedArtist.setArtistName("藤川千愛2");

    // When & Then
    performUpdateRequest(expectedArtist)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    ArtistEntity artist = artistMapper.findOne(1);
    Assertions.assertEquals(artist, expectedArtist);
  }

  private ResultActions performUpdateRequest(ArtistEntity artist) throws Exception {
    return mockMvc
        .perform(
            post(UPDATE_ENDPOINT)
                .with(csrf())
                .param("update", "")
                .param("artistId", artist.getArtistId().toString())
                .param("artistName", artist.getArtistName()))
        .andDo(print());
  }

  @Test
  void delete_ExistingArtist_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    ArtistEntity targetArtist = artistMapper.findOne(1);

    // When & Then
    performDeleteRequest(targetArtist.getArtistId())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    ArtistEntity artist = artistMapper.findOne(1);
    Assertions.assertNull(artist);
  }

  private ResultActions performDeleteRequest(int artistId) throws Exception {
    return mockMvc
        .perform(
            post(DELETE_ENDPOINT)
                .with(csrf())
                .param("delete", "")
                .param("artistId", String.valueOf(artistId)))
        .andDo(print());
  }
}
