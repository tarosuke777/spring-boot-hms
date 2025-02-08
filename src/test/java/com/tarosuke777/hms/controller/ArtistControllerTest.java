package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
import com.tarosuke777.hms.form.ArtistForm;
import com.tarosuke777.hms.repository.ArtistMapper;
import com.tarosuke777.hms.repository.TestArtistMapper;

/**
 * https://spring.pleiades.io/spring-framework/reference/testing/testcontext-framework/tx.html
 * https://spring.pleiades.io/spring-framework/reference/testing/testcontext-framework/executing-sql.html#testcontext-executing-sql-declaratively-tx
 * https://spring.pleiades.io/spring-boot/reference/features/logging.html
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class ArtistControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ArtistMapper artistMapper;
  @Autowired private TestArtistMapper testArtistMapper;

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
        artistMapper.findMany().stream()
            .map(entity -> new ArtistForm(entity.getArtistId(), entity.getArtistName()))
            .collect(Collectors.toList());

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
    ArtistEntity expectedArtistEntity = testArtistMapper.findFirstOne();
    ArtistForm expectedArtistForm =
        new ArtistForm(expectedArtistEntity.getArtistId(), expectedArtistEntity.getArtistName());

    // When & Then
    performGetDetailRequest(expectedArtistEntity.getArtistId())
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

    // When & Then
    performRegisterRequest(artistName)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    ArtistEntity artistEntity = testArtistMapper.findLastOne();
    Assertions.assertEquals(artistName, artistEntity.getArtistName());
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
    ArtistEntity expectedArtist = testArtistMapper.findFirstOne();
    expectedArtist.setArtistName("藤川千愛2");

    // When & Then
    performUpdateRequest(expectedArtist)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    ArtistEntity artist = testArtistMapper.findFirstOne();
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
    ArtistEntity targetArtist = testArtistMapper.findFirstOne();

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
