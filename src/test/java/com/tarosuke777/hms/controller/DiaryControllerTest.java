package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
import com.tarosuke777.hms.entity.DiaryEntity;
import com.tarosuke777.hms.form.DiaryForm;
import com.tarosuke777.hms.repository.DiaryRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class DiaryControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private DiaryRepository diaryRepository; // Repositoryへ変更
  @Autowired
  private ModelMapper modelMapper; // ModelMapper追加
  @Autowired
  private EntityManager entityManager; // キャッシュクリア用

  private static final String LIST_ENDPOINT = "/diary/list";
  private static final String LIST_VIEW = "diary/list";
  private static final String LIST_URL = "/diary/list";

  private static final String DETAIL_ENDPOINT = "/diary/detail/{id}";
  private static final String DETAIL_VIEW = "diary/detail";

  private static final String REGISTER_ENDPOINT = "/diary/register";
  private static final String REGISTER_VIEW = "diary/register";

  private static final String UPDATE_ENDPOINT = "/diary/detail";
  private static final String DELETE_ENDPOINT = "/diary/detail";

  @Test
  void getList_ShouldReturnDiaryList() throws Exception {

    // Given
    List<DiaryForm> expectedDiaryList =
        diaryRepository.findByCreatedBy("admin", Sort.by("diaryDate").descending()).stream()
            .map(entity -> modelMapper.map(entity, DiaryForm.class)).toList();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("diaryList", expectedDiaryList))
        .andExpect(view().name(LIST_VIEW));
  }


  @Test
  void getDetail_ShouldReturnDiaryDetail() throws Exception {

    // Given
    DiaryEntity entity = diaryRepository.findAll().getFirst();
    DiaryForm expectedDiaryForm = modelMapper.map(entity, DiaryForm.class);

    // When & Then
    performGetDetailRequest(entity.getDiaryId()).andExpect(status().isOk())
        .andExpect(model().attribute("diaryForm", expectedDiaryForm))
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
    DiaryForm form = new DiaryForm();
    form.setDiaryDate(LocalDate.of(2026, 1, 23));
    form.setTodoPlan("Java学習");
    form.setTodoActual("完了");
    form.setFunPlan(5);
    form.setFunActual(4);
    form.setCommentPlan("集中する");
    form.setCommentActual("頑張った");

    // When & Then
    performRegisterRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    DiaryEntity lastDiary = diaryRepository.findAll().getLast();
    Assertions.assertAll(
        () -> Assertions.assertEquals(form.getDiaryDate(), lastDiary.getDiaryDate()),
        () -> Assertions.assertEquals(form.getTodoPlan(), lastDiary.getTodoPlan()),
        () -> Assertions.assertEquals(form.getTodoActual(), lastDiary.getTodoActual()),
        () -> Assertions.assertEquals(form.getFunPlan(), lastDiary.getFunPlan()),
        () -> Assertions.assertEquals(form.getFunActual(), lastDiary.getFunActual()),
        () -> Assertions.assertEquals(form.getCommentPlan(), lastDiary.getCommentPlan()),
        () -> Assertions.assertEquals(form.getCommentActual(), lastDiary.getCommentActual()));
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    DiaryEntity diary = diaryRepository.findAll().getFirst();

    DiaryForm form = modelMapper.map(diary, DiaryForm.class);
    form.setTodoPlan("update Todo Plan");

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    DiaryEntity updatedDiary = diaryRepository.findById(form.getDiaryId()).orElse(null);
    Assertions.assertEquals(form.getTodoPlan(), updatedDiary.getTodoPlan());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    DiaryEntity diary = diaryRepository.findAll().getFirst();
    Integer currentId = diary.getDiaryId();
    Integer currentVersion = diary.getVersion(); // 現在のバージョンを取得

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    diary.setTodoPlan("Concurrent Update");
    diaryRepository.saveAndFlush(diary); // これでDB上のバージョンが上がる
    entityManager.clear();

    DiaryForm form = new DiaryForm();
    form.setDiaryId(currentId);
    form.setDiaryDate(diary.getDiaryDate());
    form.setTodoPlan("Try to Update");
    form.setVersion(currentVersion);

    // When & Then
    performUpdateRequest(form).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void delete_ExistingDiary_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    DiaryEntity targetDiary = diaryRepository.findAll().getFirst();
    Integer targetDiaryId = targetDiary.getDiaryId();

    // When & Then
    performDeleteRequest(targetDiary.getDiaryId()).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    DiaryEntity diary = diaryRepository.findById(targetDiaryId).orElse(null);
    Assertions.assertNull(diary);
  }

  // --- Helper Methods ---

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int diaryId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, diaryId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(DiaryForm form) throws Exception {
    return mockMvc.perform(
        post(REGISTER_ENDPOINT).with(csrf()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("diaryDate", form.getDiaryDate().toString())
            .param("todoPlan", form.getTodoPlan()).param("todoActual", form.getTodoActual())
            .param("funPlan", String.valueOf(form.getFunPlan()))
            .param("funActual", String.valueOf(form.getFunActual()))
            .param("commentPlan", form.getCommentPlan())
            .param("commentActual", form.getCommentActual()))
        .andDo(print());
  }

  private ResultActions performUpdateRequest(DiaryForm form) throws Exception {
    return mockMvc
        .perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
            .param("diaryId", form.getDiaryId().toString())
            .param("diaryDate", form.getDiaryDate().toString())
            .param("todoPlan", form.getTodoPlan()).param("version", form.getVersion().toString()))
        .andDo(print());
  }

  private ResultActions performDeleteRequest(int diaryId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("diaryId",
        String.valueOf(diaryId))).andDo(print());
  }

}
