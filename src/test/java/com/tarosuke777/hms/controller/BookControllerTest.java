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
import org.modelmapper.ModelMapper;
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
import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.form.BookForm;
import com.tarosuke777.hms.repository.AuthorRepository;
import com.tarosuke777.hms.repository.BookRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private AuthorRepository authorRepository;
  @Autowired
  private ModelMapper modelMapper;
  @Autowired
  private EntityManager entityManager;

  private static final String LIST_ENDPOINT = "/book/list";
  private static final String LIST_VIEW = "book/list";
  private static final String LIST_URL = "/book/list";

  private static final String DETAIL_ENDPOINT = "/book/detail/{id}";
  private static final String DETAIL_VIEW = "book/detail";

  private static final String REGISTER_ENDPOINT = "/book/register";
  private static final String REGISTER_VIEW = "book/register";

  private static final String UPDATE_ENDPOINT = "/book/detail";
  private static final String DELETE_ENDPOINT = "/book/detail";

  @Test
  void getList_ShouldReturnBookListAndAuthorMap() throws Exception {

    // Given
    List<BookForm> expectedBookList = bookRepository.findAll().stream()
        .map(entity -> modelMapper.map(entity, BookForm.class)).toList();

    Map<Integer, String> expectedAuthorMap = getAuthorMap();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("authorMap", expectedAuthorMap))
        .andExpect(model().attribute("bookList", expectedBookList))
        .andExpect(view().name(LIST_VIEW));
  }


  @Test
  void getDetail_ShouldReturnBookDetailAndAuthorMap() throws Exception {
    // Given
    BookEntity bookEntity = bookRepository.findAll().get(0);
    BookForm expectedBookForm = modelMapper.map(bookEntity, BookForm.class);
    expectedBookForm.setAuthorId(bookEntity.getAuthor().getAuthorId());
    Map<Integer, String> expectedAuthorMap = getAuthorMap();

    // When & Then
    performGetDetailRequest(bookEntity.getBookId()).andExpect(status().isOk())
        .andExpect(model().attribute("authorMap", expectedAuthorMap))
        .andExpect(model().attribute("bookForm", expectedBookForm))
        .andExpect(view().name(DETAIL_VIEW)).andExpect(model().hasNoErrors());
  }


  @Test
  void getRegister_ShouldReturnRegisterPageWithAuthorMap() throws Exception {

    // Given
    Map<Integer, String> expectedAuthorMap = getAuthorMap();
    // When & Then
    performGetRegisterRequest().andExpect(status().isOk())
        .andExpect(model().attribute("authorMap", expectedAuthorMap))
        .andExpect(view().name(REGISTER_VIEW)).andExpect(model().hasNoErrors());
  }


  @Test
  void register_WithValidData_ShouldRedirectToList() throws Exception {

    // Given
    AuthorEntity authorEntity = authorRepository.findAll().get(0);
    BookForm bookForm = new BookForm(null, "test", authorEntity.getAuthorId(), null, null);

    // When & Then
    performRegisterRequest(bookForm).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    List<BookEntity> books = bookRepository.findAll();
    BookEntity savedBook = books.get(books.size() - 1);

    Assertions.assertEquals(bookForm.getBookName(), savedBook.getBookName());
    Assertions.assertEquals(bookForm.getAuthorId(), savedBook.getAuthor().getAuthorId());
  }


  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    BookEntity expectedBook = bookRepository.findAll().get(0);
    expectedBook.setBookName("更新後の本タイトル");

    // When & Then
    performUpdateRequest(expectedBook).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    BookEntity book = bookRepository.findById(expectedBook.getBookId()).orElse(null);
    assertEquals(book.getBookName(), expectedBook.getBookName());
  }


  @Test
  void delete_ExistingBook_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    BookEntity expectedBook = bookRepository.findAll().get(0);

    // When & Then
    performDeleteRequest(expectedBook.getBookId()).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    BookEntity book = bookRepository.findById(expectedBook.getBookId()).orElse(null);
    assertNull(book);
  }

  // --- Helper Methods ---
  private Map<Integer, String> getAuthorMap() {
    return authorRepository.findAll().stream().collect(Collectors.toMap(AuthorEntity::getAuthorId,
        AuthorEntity::getAuthorName, (existing, replacement) -> existing, LinkedHashMap::new));
  }

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int bookId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, bookId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(BookForm form) throws Exception {
    return mockMvc.perform(post(REGISTER_ENDPOINT).with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("bookName", form.getBookName())
        .param("authorId", String.valueOf(form.getAuthorId()))).andDo(print());
  }

  private ResultActions performUpdateRequest(BookEntity book) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("bookId", book.getBookId().toString()).param("bookName", book.getBookName())
        .param("authorId", book.getAuthor().getAuthorId().toString())).andDo(print());
  }

  private ResultActions performDeleteRequest(int bookId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("bookId",
        String.valueOf(bookId))).andDo(print());
  }
}
