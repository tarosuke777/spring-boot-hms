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

import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.form.AuthorForm;
import com.tarosuke777.hms.repository.AuthorMapper;
import com.tarosuke777.hms.repository.TestAuthorMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class AuthorControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private AuthorMapper authorMapper;
  @Autowired private TestAuthorMapper testAuthorMapper;

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
        authorMapper.findMany().stream()
            .map(entity -> new AuthorForm(entity.getAuthorId(), entity.getAuthorName()))
            .collect(Collectors.toList());

    // When & Then
    performGetListRequest()
        .andExpect(status().isOk())
        .andExpect(model().attribute("authorList", expectedAuthorList))
        .andExpect(view().name(LIST_VIEW));
  }

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  @Test
  void getDetail_ShouldReturnAuthorDetail() throws Exception {

    // Given
    AuthorEntity expectedAuthorEntity = testAuthorMapper.findFirstOne();
    AuthorForm expectedAuthorForm =
        new AuthorForm(expectedAuthorEntity.getAuthorId(), expectedAuthorEntity.getAuthorName());

    // When & Then
    performGetDetailRequest(expectedAuthorEntity.getAuthorId())
        .andExpect(status().isOk())
        .andExpect(model().attribute("authorForm", expectedAuthorForm))
        .andExpect(view().name(DETAIL_VIEW))
        .andExpect(model().hasNoErrors());
  }

  private ResultActions performGetDetailRequest(int authorId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, authorId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
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
    String authorName = "TestAuthorName";

    // When & Then
    performRegisterRequest(authorName)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    AuthorEntity authorEntity = testAuthorMapper.findLastOne();
    Assertions.assertEquals(authorName, authorEntity.getAuthorName());
  }

  private ResultActions performRegisterRequest(String authorName) throws Exception {
    return mockMvc
        .perform(
            post(REGISTER_ENDPOINT)
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("authorName", authorName))
        .andDo(print());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    AuthorEntity expectedAuthor = testAuthorMapper.findFirstOne();
    expectedAuthor.setAuthorName("著者２");

    // When & Then
    performUpdateRequest(expectedAuthor)
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    AuthorEntity author = testAuthorMapper.findFirstOne();
    Assertions.assertEquals(author, expectedAuthor);
  }

  private ResultActions performUpdateRequest(AuthorEntity author) throws Exception {
    return mockMvc
        .perform(
            post(UPDATE_ENDPOINT)
                .with(csrf())
                .param("update", "")
                .param("authorId", author.getAuthorId().toString())
                .param("authorName", author.getAuthorName()))
        .andDo(print());
  }

  @Test
  void delete_ExistingAuthor_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    AuthorEntity targetAuthor = testAuthorMapper.findFirstOne();

    // When & Then
    performDeleteRequest(targetAuthor.getAuthorId())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    AuthorEntity author = authorMapper.findOne(1);
    Assertions.assertNull(author);
  }

  private ResultActions performDeleteRequest(int authorId) throws Exception {
    return mockMvc
        .perform(
            post(DELETE_ENDPOINT)
                .with(csrf())
                .param("delete", "")
                .param("authorId", String.valueOf(authorId)))
        .andDo(print());
  }
}
