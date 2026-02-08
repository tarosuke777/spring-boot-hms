package com.tarosuke777.hms.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
import com.tarosuke777.hms.entity.MusicEntity;
import com.tarosuke777.hms.form.MusicForm;
import com.tarosuke777.hms.mapper.MusicMapper;
import com.tarosuke777.hms.repository.ArtistRepository;
import com.tarosuke777.hms.repository.MusicRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class MusicControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private MusicRepository musicRepository;
  @Autowired
  private ArtistRepository artistRepository;
  @Autowired
  private MusicMapper musicMapper;
  @Autowired
  private EntityManager entityManager;

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
    List<MusicForm> expectedMusicList = musicRepository.findByCreatedByOrderByMusicIdAsc("admin")
        .stream().map(musicMapper::toForm).toList();

    Map<Integer, String> expectedArtistMap = getArtistMap();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expectedArtistMap))
        .andExpect(model().attribute("musicList", expectedMusicList))
        .andExpect(view().name(LIST_VIEW));
  }


  @Test
  void getDetail_ShouldReturnMusicDetailAndArtistMap() throws Exception {
    // Given
    MusicEntity musicEntity = musicRepository.findAll().getFirst();
    MusicForm expectedMusicForm = musicMapper.toForm(musicEntity);
    expectedMusicForm.setArtistId(musicEntity.getArtist().getId());
    Map<Integer, String> expectedArtistMap = getArtistMap();

    // When & Then
    performGetDetailRequest(musicEntity.getMusicId()).andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expectedArtistMap))
        .andExpect(model().attribute("musicForm", expectedMusicForm))
        .andExpect(view().name(DETAIL_VIEW)).andExpect(model().hasNoErrors());
  }


  @Test
  void getRegister_ShouldReturnRegisterPageWithArtistMap() throws Exception {

    // Given
    Map<Integer, String> expectedArtistMap = getArtistMap();

    // When & Then
    performGetRegisterRequest().andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expectedArtistMap))
        .andExpect(view().name(REGISTER_VIEW)).andExpect(model().hasNoErrors());
  }


  @Test
  void register_WithValidData_ShouldRedirectToList() throws Exception {

    // Given
    ArtistEntity artistEntity = artistRepository.findAll().getFirst();
    MusicForm musicForm = new MusicForm(null, "test", artistEntity.getId(), null, null);

    // When & Then
    performRegisterRequest(musicForm).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    MusicEntity musicEntity = musicRepository.findAll().getLast();

    Assertions.assertEquals(musicForm.getMusicName(), musicEntity.getMusicName());
    Assertions.assertEquals(musicForm.getArtistId(), musicEntity.getArtist().getId());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    MusicEntity music = musicRepository.findAll().getFirst();

    MusicForm form = musicMapper.toForm(music);
    form.setArtistId(music.getArtist().getId());
    form.setMusicName("更新後の曲名");

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    MusicEntity updatedEntity = musicRepository.findById(form.getMusicId()).orElse(null);
    assertEquals(form.getMusicName(), updatedEntity.getMusicName());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    MusicEntity music = musicRepository.findAll().getFirst();
    Integer currentId = music.getMusicId();
    Integer currentVersion = music.getVersion(); // 現在のバージョンを取得
    ArtistEntity currentArtist = music.getArtist();

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    music.setMusicName("Concurrent Update");
    musicRepository.saveAndFlush(music); // これでDB上のバージョンが上がる
    entityManager.clear();

    MusicForm form = new MusicForm();
    form.setMusicId(currentId);
    form.setMusicName("Try to Update");
    form.setVersion(currentVersion);
    form.setArtistId(currentArtist.getId());

    // When & Then
    performUpdateRequest(form).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }


  @Test
  void delete_ExistingMusic_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    MusicEntity targetMusic = musicRepository.findAll().getFirst();

    // When & Then
    performDeleteRequest(targetMusic.getMusicId()).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush(); // 未処理のSQLを全部出す
    entityManager.clear(); // キャッシュをクリア

    MusicEntity music = musicRepository.findById(targetMusic.getMusicId()).orElse(null);
    assertNull(music);
  }

  // --- Helper Methods ---
  private Map<Integer, String> getArtistMap() {
    return artistRepository.findAll().stream().collect(Collectors.toMap(ArtistEntity::getId,
        ArtistEntity::getName, (existing, replacement) -> existing, LinkedHashMap::new));
  }

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int musicId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, musicId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(MusicForm form) throws Exception {
    return mockMvc.perform(post(REGISTER_ENDPOINT).with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("musicName", form.getMusicName())
        .param("artistId", String.valueOf(form.getArtistId()))).andDo(print());
  }

  private ResultActions performUpdateRequest(MusicForm form) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("musicId", form.getMusicId().toString()).param("musicName", form.getMusicName())
        .param("artistId", String.valueOf(form.getArtistId()))
        .param("version", form.getVersion().toString())).andDo(print());
  }

  private ResultActions performDeleteRequest(int musicId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("musicId",
        String.valueOf(musicId))).andDo(print());
  }
}
