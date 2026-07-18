package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.form.UserForm;
import com.tarosuke777.hms.mapper.UserMapper;
import com.tarosuke777.hms.repository.UserRepository;
import jakarta.persistence.EntityManager;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private EntityManager entityManager;

  private static final String LIST_ENDPOINT = "/user/list";
  private static final String LIST_VIEW = "user/list";
  private static final String LIST_URL = "/user/list";
  private static final String DETAIL_ENDPOINT = "/user/detail/{userId}";
  private static final String DETAIL_VIEW = "user/detail";
  private static final String UPDATE_ENDPOINT = "/user/detail";
  private static final String DELETE_ENDPOINT = "/user/detail";

  @Test
  void getList_ShouldReturnUserList() throws Exception {
    // Given
    List<UserForm> expectedUserList =
        userRepository.findAll().stream().map(userMapper::toForm).toList();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("userList", expectedUserList))
        .andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getDetail_ShouldReturnUserDetail() throws Exception {
    // Given
    UserEntity entity = userRepository.findAll().getFirst();
    UserForm expectedUserForm = userMapper.toForm(entity);

    // When & Then
    performGetDetailRequest(entity.getId()).andExpect(status().isOk())
        .andExpect(model().attribute("userForm", expectedUserForm))
        .andExpect(view().name(DETAIL_VIEW)).andExpect(model().hasNoErrors());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {
    // Given
    UserEntity targetEntity = userRepository.findAll().getFirst();
    UserForm form = userMapper.toForm(targetEntity);
    form.setName("Updated User Name");
    form.setPassword("newPassword123");

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    UserEntity updatedEntity = userRepository.findById(form.getId()).orElse(null);
    Assertions.assertEquals(form.getName(), updatedEntity.getName());
  }

  @Test
  void delete_ExistingUser_ShouldDeleteAndRedirectToList() throws Exception {
    // Given
    UserEntity targetUser = userRepository.findAll().getFirst();
    Integer targetUserId = targetUser.getId();

    // When & Then
    performDeleteRequest(targetUserId).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    UserEntity user = userRepository.findById(targetUserId).orElse(null);
    Assertions.assertNull(user);
  }

  // --- Helper Methods ---

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int userId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, userId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performUpdateRequest(UserForm form) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("id", form.getId().toString()).param("name", form.getName())
        .param("password", form.getPassword() != null ? form.getPassword() : "")
        .param("role", form.getRole().toString()).param("version", form.getVersion().toString()))
        .andDo(print());
  }

  private ResultActions performDeleteRequest(int userId) throws Exception {
    return mockMvc.perform(
        post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("id", String.valueOf(userId)))
        .andDo(print());
  }
}
