package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
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
import com.tarosuke777.hms.entity.TrainingMenuEntity;
import com.tarosuke777.hms.form.TrainingMenuForm;
import com.tarosuke777.hms.mapper.TrainingMenuMapper;
import com.tarosuke777.hms.repository.TrainingMenuRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class TrainingMenuControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TrainingMenuRepository trainingMenuRepository; // Repositoryへ変更
  @Autowired
  private TrainingMenuMapper trainingMenuMapper; // TrainingMenuMapper追加
  @Autowired
  private EntityManager entityManager; // キャッシュクリア用

  private static final String LIST_ENDPOINT = "/trainingMenu/list";
  private static final String LIST_VIEW = "trainingMenu/list";
  private static final String LIST_URL = "/trainingMenu/list";

  private static final String DETAIL_ENDPOINT = "/trainingMenu/detail/{id}";
  private static final String DETAIL_VIEW = "trainingMenu/detail";

  private static final String REGISTER_ENDPOINT = "/trainingMenu/register";
  private static final String REGISTER_VIEW = "trainingMenu/register";

  private static final String UPDATE_ENDPOINT = "/trainingMenu/detail";
  private static final String DELETE_ENDPOINT = "/trainingMenu/detail";

  @Test
  void getList_ShouldReturnTrainingMenuList() throws Exception {

    // Given
    List<TrainingMenuForm> expectedTrainingMenuList = trainingMenuRepository
        .findByCreatedBy("admin").stream().map(trainingMenuMapper::toForm).toList();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("trainingMenuList", expectedTrainingMenuList))
        .andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getDetail_ShouldReturnTrainingMenuDetail() throws Exception {

    // Given
    TrainingMenuEntity entity = trainingMenuRepository.findAll().getFirst();
    TrainingMenuForm expectedTrainingMenuForm = trainingMenuMapper.toForm(entity);

    // When & Then
    performGetDetailRequest(entity.getTrainingMenuId()).andExpect(status().isOk())
        .andExpect(model().attribute("trainingMenuForm", expectedTrainingMenuForm))
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
    String TrainingMenuName = "TestTrainingMenuName";

    // When & Then
    performRegisterRequest(TrainingMenuName).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    TrainingMenuEntity lastTrainingMenu = trainingMenuRepository.findAll().getLast();
    Assertions.assertEquals(TrainingMenuName, lastTrainingMenu.getTrainingMenuName());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    TrainingMenuEntity expectedTrainingMenu = trainingMenuRepository.findAll().getFirst();
    TrainingMenuForm form = trainingMenuMapper.toForm(expectedTrainingMenu);
    form.setTrainingMenuName("著者２");

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    TrainingMenuEntity updatedTrainingMenu =
        trainingMenuRepository.findById(expectedTrainingMenu.getTrainingMenuId()).orElse(null);
    Assertions.assertEquals(updatedTrainingMenu.getTrainingMenuName(),
        expectedTrainingMenu.getTrainingMenuName());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    TrainingMenuEntity TrainingMenu = trainingMenuRepository.findAll().getFirst();
    Integer currentId = TrainingMenu.getTrainingMenuId();
    Integer currentVersion = TrainingMenu.getVersion(); // 現在のバージョンを取得

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    TrainingMenu.setTrainingMenuName("Concurrent Update");
    trainingMenuRepository.saveAndFlush(TrainingMenu); // これでDB上のバージョンが上がる
    entityManager.clear();

    TrainingMenuForm form = new TrainingMenuForm();
    form.setTrainingMenuId(currentId);
    form.setTrainingMenuName("Try to Update");
    form.setVersion(currentVersion);

    // When & Then
    performUpdateRequest(form).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void delete_ExistingTrainingMenu_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    TrainingMenuEntity targetTrainingMenu = trainingMenuRepository.findAll().getFirst();
    Integer targetTrainingMenuId = targetTrainingMenu.getTrainingMenuId();

    // When & Then
    performDeleteRequest(targetTrainingMenu.getTrainingMenuId())
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    TrainingMenuEntity deletedTrainingMenu =
        trainingMenuRepository.findById(targetTrainingMenuId).orElse(null);
    Assertions.assertNull(deletedTrainingMenu);
  }

  // --- Helper Methods ---

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int TrainingMenuId) throws Exception {
    return mockMvc.perform(
        get(DETAIL_ENDPOINT, TrainingMenuId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(String TrainingMenuName) throws Exception {
    return mockMvc.perform(
        post(REGISTER_ENDPOINT).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("trainingMenuName", TrainingMenuName))
        .andDo(print());
  }

  private ResultActions performUpdateRequest(TrainingMenuForm form) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("trainingMenuId", form.getTrainingMenuId().toString())
        .param("trainingMenuName", form.getTrainingMenuName())
        .param("version", form.getVersion().toString())).andDo(print());
  }

  private ResultActions performDeleteRequest(int TrainingMenuId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "")
        .param("trainingMenuId", String.valueOf(TrainingMenuId))).andDo(print());
  }
}
