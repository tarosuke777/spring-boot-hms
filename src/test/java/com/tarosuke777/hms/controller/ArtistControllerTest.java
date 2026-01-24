package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.form.ArtistForm;
import com.tarosuke777.hms.repository.ArtistRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class ArtistControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ArtistRepository artistRepository;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private EntityManager entityManager; // キャッシュクリア用

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
    List<ArtistForm> expectedArtistList = artistRepository.findAll().stream()
        .map(entity -> modelMapper.map(entity, ArtistForm.class)).collect(Collectors.toList());

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("artistList", expectedArtistList))
        .andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getDetail_ShouldReturnArtistDetail() throws Exception {

    // Given
    ArtistEntity expectedArtistEntity = artistRepository.findAll().getFirst();
    ArtistForm expectedArtistForm = modelMapper.map(expectedArtistEntity, ArtistForm.class);

    // When & Then
    performGetDetailRequest(expectedArtistEntity.getArtistId()).andExpect(status().isOk())
        .andExpect(model().attribute("artistForm", expectedArtistForm))
        .andExpect(view().name(DETAIL_VIEW)).andExpect(model().hasNoErrors());
  }

  @Test
  void getRegister_ShouldReturnRegisterPage() throws Exception {

    // Given

    // When & Then
    performGetRegisterRequest().andExpect(status().isOk()).andExpect(view().name(REGISTER_VIEW))
        .andExpect(model().hasNoErrors());
  }


  @Test
  void register_WithValidData_ShouldRedirectToList() throws Exception {

    // Given
    String artistName = "TestArtistName";

    // When & Then
    performRegisterRequest(artistName).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    List<ArtistEntity> artists = artistRepository.findAll();
    ArtistEntity lastArtist = artists.get(artists.size() - 1);
    Assertions.assertEquals(artistName, lastArtist.getArtistName());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    ArtistEntity expectedArtist = artistRepository.findAll().getFirst();
    expectedArtist.setArtistName("UpdatedName");

    // When & Then
    performUpdateRequest(expectedArtist).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    ArtistEntity updatedEntity =
        artistRepository.findById(expectedArtist.getArtistId()).orElse(null);
    Assertions.assertEquals(updatedEntity.getArtistName(), expectedArtist.getArtistName());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    ArtistEntity artist = artistRepository.findAll().getFirst();
    Integer currentId = artist.getArtistId();
    Integer currentVersion = artist.getVersion(); // 現在のバージョンを取得

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    artist.setArtistName("Concurrent Update");
    artistRepository.saveAndFlush(artist); // これでDB上のバージョンが上がる
    entityManager.clear();

    ArtistEntity artistToUpdate = new ArtistEntity();
    artistToUpdate.setArtistId(currentId);
    artistToUpdate.setArtistName("Try to Update");
    artistToUpdate.setVersion(currentVersion); // 古いバージョンをセット

    // When & Then
    performUpdateRequest(artistToUpdate).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void delete_ExistingArtist_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    ArtistEntity targetArtist = artistRepository.findAll().getFirst();
    Integer targetArtistId = targetArtist.getArtistId();

    // When & Then
    performDeleteRequest(targetArtistId).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    ArtistEntity artist = artistRepository.findById(targetArtistId).orElse(null);
    Assertions.assertNull(artist);
  }

  // --- Helper Methods ---

  private ResultActions performGetDetailRequest(int artistId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, artistId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(String artistName) throws Exception {
    return mockMvc
        .perform(post(REGISTER_ENDPOINT).with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("artistName", artistName))
        .andDo(print());
  }

  private ResultActions performUpdateRequest(ArtistEntity artist) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("artistId", artist.getArtistId().toString())
        .param("artistName", artist.getArtistName())
        .param("version", artist.getVersion().toString())).andDo(print());
  }

  private ResultActions performDeleteRequest(int artistId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("artistId",
        String.valueOf(artistId))).andDo(print());
  }
}
