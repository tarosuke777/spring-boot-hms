package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
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
import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.form.AuthorForm;
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
  private ModelMapper modelMapper; // ModelMapper追加
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
    List<AuthorForm> expectedAuthorList = authorRepository.findAll().stream()
        .map(entity -> modelMapper.map(entity, AuthorForm.class)).toList();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("authorList", expectedAuthorList))
        .andExpect(view().name(LIST_VIEW));
  }


  @Test
  void getDetail_ShouldReturnAuthorDetail() throws Exception {

    // Given
    AuthorEntity entity = authorRepository.findAll().getFirst();
    AuthorForm expectedAuthorForm = modelMapper.map(entity, AuthorForm.class);

    // When & Then
    performGetDetailRequest(entity.getAuthorId()).andExpect(status().isOk())
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
    Assertions.assertEquals(authorName, lastAuthor.getAuthorName());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    AuthorEntity expectedAuthor = authorRepository.findAll().getFirst();
    expectedAuthor.setAuthorName("著者２");

    // When & Then
    performUpdateRequest(expectedAuthor).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    AuthorEntity author = authorRepository.findById(expectedAuthor.getAuthorId()).orElse(null);
    Assertions.assertEquals(author.getAuthorName(), expectedAuthor.getAuthorName());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    AuthorEntity author = authorRepository.findAll().getFirst();
    Integer currentId = author.getAuthorId();
    Integer currentVersion = author.getVersion(); // 現在のバージョンを取得

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    author.setAuthorName("Concurrent Update");
    authorRepository.saveAndFlush(author); // これでDB上のバージョンが上がる
    entityManager.clear();

    AuthorEntity authorToUpdate = new AuthorEntity();
    authorToUpdate.setAuthorId(currentId);
    authorToUpdate.setAuthorName("Try to Update");
    authorToUpdate.setVersion(currentVersion); // 古いバージョンをセット

    // When & Then
    performUpdateRequest(authorToUpdate).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void delete_ExistingAuthor_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    AuthorEntity targetAuthor = authorRepository.findAll().getFirst();
    Integer targetAuthorId = targetAuthor.getAuthorId();

    // When & Then
    performDeleteRequest(targetAuthor.getAuthorId()).andExpect(status().is3xxRedirection())
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

  private ResultActions performRegisterRequest(String authorName) throws Exception {
    return mockMvc
        .perform(post(REGISTER_ENDPOINT).with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("authorName", authorName))
        .andDo(print());
  }

  private ResultActions performUpdateRequest(AuthorEntity author) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("authorId", author.getAuthorId().toString())
        .param("authorName", author.getAuthorName())
        .param("version", author.getVersion().toString())).andDo(print());
  }

  private ResultActions performDeleteRequest(int authorId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("authorId",
        String.valueOf(authorId))).andDo(print());
  }
}
