package com.tarosuke777.hms.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.TrainingEntity;
import com.tarosuke777.hms.entity.TrainingMenuEntity;
import com.tarosuke777.hms.enums.TargetArea;
import com.tarosuke777.hms.form.SelectOptionTrainingMenu;
import com.tarosuke777.hms.form.TrainingForm;
import com.tarosuke777.hms.mapper.TrainingMapper;
import com.tarosuke777.hms.repository.TrainingMenuRepository;
import com.tarosuke777.hms.repository.TrainingRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class TrainingControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TrainingRepository trainingRepository;
  @Autowired
  private TrainingMenuRepository trainingMenuRepository;
  @Autowired
  private TrainingMapper trainingMapper;
  @Autowired
  private EntityManager entityManager;

  private static final String LIST_ENDPOINT = "/training/list";
  private static final String LIST_VIEW = "training/list";
  private static final String LIST_URL = "/training/list";

  private static final String DETAIL_ENDPOINT = "/training/detail/{id}";
  private static final String DETAIL_VIEW = "training/detail";

  private static final String REGISTER_ENDPOINT = "/training/register";
  private static final String REGISTER_VIEW = "training/register";

  private static final String UPDATE_ENDPOINT = "/training/detail";
  private static final String DELETE_ENDPOINT = "/training/detail";

  @Test
  void getList_ShouldReturnTrainingList() throws Exception {

    // Given
    List<TrainingForm> expectedTrainingList =
        trainingRepository.findByCreatedBy("admin", Sort.by("trainingDate").descending()).stream()
            .map(this::convertToForm).toList();
    Map<Integer, String> expectedTrainingAreaMap = TargetArea.getTargetAreaMap();
    Map<Integer, String> expectedTrainingMenuMap = getTrainingMenuMap();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("trainingAreaMap", expectedTrainingAreaMap))
        .andExpect(model().attribute("trainingMenuMap", expectedTrainingMenuMap))
        .andExpect(model().attribute("trainingList", expectedTrainingList))
        .andExpect(view().name(LIST_VIEW));
  }


  @Test
  void getDetail_ShouldReturnTrainingDetail() throws Exception {
    // Given
    TrainingForm expectedTrainingForm =
        trainingRepository.findAll().stream().findFirst().map(this::convertToForm).orElseThrow();
    Map<Integer, String> expectedTrainingMenuMap = getTrainingMenuMap();

    // When & Then
    performGetDetailRequest(expectedTrainingForm.getTrainingId()).andExpect(status().isOk())
        .andExpect(model().attribute("trainingMenuMap", expectedTrainingMenuMap))
        .andExpect(model().attribute("trainingForm", expectedTrainingForm))
        .andExpect(view().name(DETAIL_VIEW)).andExpect(model().hasNoErrors());
  }


  @Test
  void getRegister_ShouldReturnRegisterPage() throws Exception {

    // Given
    List<SelectOptionTrainingMenu> trainingMenuSelectList = getTrainingMenuSelectList();
    Map<Integer, String> trainingTargetAreaMap = TargetArea.getTargetAreaMap();

    // When & Then
    performGetRegisterRequest().andExpect(status().isOk())
        .andExpect(model().attribute("trainingMenuSelectList", trainingMenuSelectList))
        .andExpect(model().attribute("trainingTargetAreaMap", trainingTargetAreaMap))
        .andExpect(view().name(REGISTER_VIEW)).andExpect(model().hasNoErrors());
  }


  @Test
  void register_WithValidData_ShouldRedirectToList() throws Exception {

    // Given
    TrainingMenuEntity trainingMenuEntity = trainingMenuRepository.findAll().getFirst();

    // LocalDateを使用するため、テスト用の日付を準備します
    LocalDate testDate = LocalDate.now();
    TrainingForm trainingForm = new TrainingForm(null, // trainingId
        testDate, // trainingDate (String "test" から修正)
        1, // trainingAreaId (適切なID、またはnull)
        trainingMenuEntity.getTrainingMenuId(), // trainingMenuId
        60, // weight (例: 60kg)
        10, // reps (例: 10回)
        3, // sets (例: 3セット)
        0 // version (例: 0)
    );


    // When & Then
    performRegisterRequest(trainingForm).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    TrainingEntity savedTraining = trainingRepository.findAll().getLast();

    Assertions.assertEquals(trainingForm.getTrainingDate(), savedTraining.getTrainingDate());
    Assertions.assertEquals(trainingForm.getTrainingMenuId(),
        savedTraining.getTrainingMenu().getTrainingMenuId());
    Assertions.assertEquals(trainingForm.getWeight(), savedTraining.getWeight());
    Assertions.assertEquals(trainingForm.getReps(), savedTraining.getReps());
    Assertions.assertEquals(trainingForm.getSets(), savedTraining.getSets());
  }


  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    TrainingEntity training = trainingRepository.findAll().getFirst();
    LocalDate newDate = training.getTrainingDate().plusDays(1);
    Integer newWeight = 100; // 例: 重量を100に更新

    TrainingForm form = convertToForm(training);
    form.setTrainingDate(newDate);
    form.setWeight(newWeight);

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    TrainingEntity updatedTraining = trainingRepository.findById(form.getTrainingId()).orElse(null);
    Assertions.assertEquals(newDate, updatedTraining.getTrainingDate());
    Assertions.assertEquals(newWeight, updatedTraining.getWeight());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    TrainingEntity trainingEntity = trainingRepository.findAll().getFirst();
    Integer currentId = trainingEntity.getTrainingId();
    Integer currentVersion = trainingEntity.getVersion(); // 現在のバージョンを取得
    TrainingMenuEntity currentTrainingMenu = trainingEntity.getTrainingMenu();

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    trainingEntity.setTrainingDate(trainingEntity.getTrainingDate().plusDays(1));
    trainingRepository.saveAndFlush(trainingEntity); // これでDB上のバージョンが上がる
    entityManager.clear();

    TrainingForm form = new TrainingForm();
    form.setTrainingId(currentId);
    form.setTrainingDate(trainingEntity.getTrainingDate().plusDays(1));
    form.setVersion(currentVersion);
    form.setTrainingMenuId(currentTrainingMenu.getTrainingMenuId());
    form.setWeight(999);
    form.setReps(999);
    form.setSets(999);

    // When & Then
    performUpdateRequest(form).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void delete_ExistingTraining_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    TrainingEntity expectedTraining = trainingRepository.findAll().getFirst();

    // When & Then
    performDeleteRequest(expectedTraining.getTrainingId()).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    TrainingEntity training =
        trainingRepository.findById(expectedTraining.getTrainingId()).orElse(null);
    assertNull(training);
  }

  // --- Helper Methods ---

  /** Entity から Form への変換（リレーションのID詰め替え含む） */
  private TrainingForm convertToForm(TrainingEntity entity) {
    TrainingForm form = trainingMapper.toForm(entity);
    if (entity.getTrainingMenu() != null) {
      form.setTrainingMenuId(entity.getTrainingMenu().getTrainingMenuId());
      form.setTrainingAreaId(entity.getTrainingMenu().getTargetAreaId());
    }
    return form;
  }

  private Map<Integer, String> getTrainingMenuMap() {
    return trainingMenuRepository.findAll().stream()
        .collect(Collectors.toMap(TrainingMenuEntity::getTrainingMenuId,
            TrainingMenuEntity::getTrainingMenuName, (existing, replacement) -> existing,
            LinkedHashMap::new));
  }

  public List<SelectOptionTrainingMenu> getTrainingMenuSelectList() {
    return trainingMenuRepository.findAll().stream()
        .map(entity -> new SelectOptionTrainingMenu(entity.getTrainingMenuId().toString(),
            entity.getTrainingMenuName(), entity.getTargetAreaId(), entity.getMaxWeight(),
            entity.getMaxReps(), entity.getMaxSets()))
        .collect(Collectors.toList());
  }

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int trainingId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, trainingId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(TrainingForm form) throws Exception {
    return mockMvc.perform(
        post(REGISTER_ENDPOINT).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("trainingDate", form.getTrainingDate().toString())
            .param("trainingAreaId", String.valueOf(form.getTrainingAreaId()))
            .param("trainingMenuId", String.valueOf(form.getTrainingMenuId()))
            .param("weight", String.valueOf(form.getWeight()))
            .param("reps", String.valueOf(form.getReps()))
            .param("sets", String.valueOf(form.getSets())))
        .andDo(print());
  }

  private ResultActions performUpdateRequest(TrainingForm form) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("trainingId", form.getTrainingId().toString())
        .param("trainingDate", form.getTrainingDate().toString())
        .param("trainingMenuId", String.valueOf(form.getTrainingMenuId()))
        .param("weight", String.valueOf(form.getWeight()))
        .param("reps", String.valueOf(form.getReps())).param("sets", String.valueOf(form.getSets()))
        .param("version", String.valueOf(form.getVersion()))).andDo(print());
  }

  private ResultActions performDeleteRequest(int trainingId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "")
        .param("trainingId", String.valueOf(trainingId))).andDo(print());
  }
}
