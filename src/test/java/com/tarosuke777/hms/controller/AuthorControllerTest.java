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
import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.form.AuthorForm;
import com.tarosuke777.hms.mapper.AuthorMapper;
import com.tarosuke777.hms.repository.AuthorRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class AuthorControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private AuthorRepository authorRepository; // Repositoryへ変更
  @Autowired
  private AuthorMapper authorMapper;
  @Autowired
  private EntityManager entityManager; // キャッシュクリア用

  private static final String LIST_ENDPOINT = "/author/list";
  private static final String LIST_VIEW = "author/list";
  private static final String LIST_URL = "/author/list";

  private static final String DETAIL_ENDPOINT = "/author/detail/{id}";
  private static final String DETAIL_VIEW = "author/detail";

  private static final String REGISTER_ENDPOINT = "/author/register";
  private static final String REGISTER_VIEW = "author/register";

  private static final String UPDATE_ENDPOINT = "/author/detail";
  private static final String DELETE_ENDPOINT = "/author/detail";

  @Test
  void getList_ShouldReturnAuthorList() throws Exception {

    // Given
    List<AuthorForm> expectedAuthorList =
        authorRepository.findByCreatedBy("admin").stream().map(authorMapper::toForm).toList();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("authorList", expectedAuthorList))
        .andExpect(view().name(LIST_VIEW));
  }


  @Test
  void getDetail_ShouldReturnAuthorDetail() throws Exception {

    // Given
    AuthorEntity entity = authorRepository.findAll().getFirst();
    AuthorForm expectedAuthorForm = authorMapper.toForm(entity);

    // When & Then
    performGetDetailRequest(entity.getId()).andExpect(status().isOk())
        .andExpect(model().attribute("authorForm", expectedAuthorForm))
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
    String authorName = "TestAuthorName";

    // When & Then
    performRegisterRequest(authorName).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    AuthorEntity lastAuthor = authorRepository.findAll().getLast();
    Assertions.assertEquals(authorName, lastAuthor.getName());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    AuthorEntity targetEntity = authorRepository.findAll().getFirst();

    AuthorForm form = authorMapper.toForm(targetEntity);
    form.setName("著者２");

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    AuthorEntity updatedEntity = authorRepository.findById(form.getId()).orElse(null);
    Assertions.assertEquals(form.getName(), updatedEntity.getName());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    AuthorEntity targetEntity = authorRepository.findAll().getFirst();
    Integer currentId = targetEntity.getId();
    Integer currentVersion = targetEntity.getVersion(); // 現在のバージョンを取得

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    targetEntity.setName("Concurrent Update");
    authorRepository.saveAndFlush(targetEntity); // これでDB上のバージョンが上がる
    entityManager.clear();

    AuthorForm form = new AuthorForm();
    form.setId(currentId);
    form.setName("Try to Update");
    form.setVersion(currentVersion); // 古いバージョンをセット

    // When & Then
    performUpdateRequest(form).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void delete_ExistingAuthor_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    AuthorEntity targetAuthor = authorRepository.findByCreatedBy("admin").getFirst();
    Integer targetAuthorId = targetAuthor.getId();

    // When & Then
    performDeleteRequest(targetAuthorId).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    AuthorEntity author = authorRepository.findById(targetAuthorId).orElse(null);
    Assertions.assertNull(author);
  }

  // --- Helper Methods ---

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int authorId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, authorId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(String name) throws Exception {
    return mockMvc.perform(post(REGISTER_ENDPOINT).with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", name)).andDo(print());
  }

  private ResultActions performUpdateRequest(AuthorForm form) throws Exception {
    return mockMvc.perform(
        post(UPDATE_ENDPOINT).with(csrf()).param("update", "").param("id", form.getId().toString())
            .param("name", form.getName()).param("version", form.getVersion().toString()))
        .andDo(print());
  }

  private ResultActions performDeleteRequest(int id) throws Exception {
    return mockMvc
        .perform(
            post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("id", String.valueOf(id)))
        .andDo(print());
  }
}
