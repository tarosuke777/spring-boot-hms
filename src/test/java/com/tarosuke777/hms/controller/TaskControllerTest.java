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
import com.tarosuke777.hms.entity.TaskEntity;
import com.tarosuke777.hms.form.TaskForm;
import com.tarosuke777.hms.repository.TaskRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private EntityManager entityManager; // キャッシュクリア用

  private static final String LIST_ENDPOINT = "/task/list";
  private static final String LIST_VIEW = "task/list";
  private static final String LIST_URL = "/task/list";

  private static final String REGISTER_ENDPOINT = "/task/create";
  private static final String REGISTER_VIEW_ENDPOINT = "/task/register";
  private static final String REGISTER_VIEW = "task/register";

  private static final String UPDATE_ENDPOINT = "/task/update";
  private static final String DELETE_ENDPOINT = "/task/delete";

  @Test
  void getList_ShouldReturnTaskList() throws Exception {

    // Given
    List<TaskForm> expectedTaskList = taskRepository.findByCreatedBy("admin").stream()
        .map(entity -> modelMapper.map(entity, TaskForm.class)).collect(Collectors.toList());

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("tasks", expectedTaskList)).andExpect(view().name(LIST_VIEW));
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
    String taskName = "TestTaskName";

    // When & Then
    performRegisterRequest(taskName).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    List<TaskEntity> tasks = taskRepository.findAll();
    TaskEntity lastTask = tasks.get(tasks.size() - 1);
    Assertions.assertEquals(taskName, lastTask.getName());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    TaskEntity task = taskRepository.findAll().getFirst();
    TaskForm form = modelMapper.map(task, TaskForm.class);
    form.setName("UpdatedName");

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    TaskEntity updatedEntity = taskRepository.findById(form.getId()).orElse(null);
    Assertions.assertEquals(updatedEntity.getName(), form.getName());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    TaskEntity task = taskRepository.findAll().getFirst();
    Integer currentId = task.getId();
    Integer currentVersion = task.getVersion(); // 現在のバージョンを取得

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    task.setName("Concurrent Update");
    taskRepository.saveAndFlush(task); // これでDB上のバージョンが上がる
    entityManager.clear();

    TaskForm form = new TaskForm();
    form.setId(currentId);
    form.setName("Try to Update");
    form.setVersion(currentVersion);

    // When & Then
    performUpdateRequest(form).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void delete_ExistingTask_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    TaskEntity targetTask = taskRepository.findAll().getFirst();
    Integer targetTaskId = targetTask.getId();

    // When & Then
    performDeleteRequest(targetTaskId).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    TaskEntity task = taskRepository.findById(targetTaskId).orElse(null);
    Assertions.assertNull(task);
  }

  // --- Helper Methods ---

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_VIEW_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(String name) throws Exception {
    return mockMvc.perform(post(REGISTER_ENDPOINT).with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", name)).andDo(print());
  }

  private ResultActions performUpdateRequest(TaskForm form) throws Exception {
    return mockMvc.perform(
        post(UPDATE_ENDPOINT).with(csrf()).param("update", "").param("id", form.getId().toString())
            .param("name", form.getName()).param("version", form.getVersion().toString()))
        .andDo(print());
  }

  private ResultActions performDeleteRequest(int taskId) throws Exception {
    return mockMvc.perform(
        post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("id", String.valueOf(taskId)))
        .andDo(print());
  }
}
