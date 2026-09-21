package arpa.home.hms.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import arpa.home.hms.entity.BookEntity;
import arpa.home.hms.entity.BookReadingLogEntity;
import arpa.home.hms.repository.BookReadingLogRepository;
import arpa.home.hms.repository.BookRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class BookReadingLogControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private BookReadingLogRepository bookReadingLogRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private EntityManager entityManager;

  @Test
  void getList_ShouldReturnBookReadingLogList() throws Exception {
    mockMvc.perform(get("/bookReadingLog/list")).andExpect(status().isOk())
        .andExpect(model().attributeExists("bookMap"))
        .andExpect(model().attributeExists("bookReadingLogPage"))
        .andExpect(view().name("bookReadingLog/list"));
  }

  @Test
  void register_WithValidData_ShouldRedirectToList() throws Exception {
    BookEntity book = bookRepository.findByCreatedByOrderByIdAsc(1).getFirst();

    mockMvc
        .perform(post("/bookReadingLog/register").with(csrf())
            .param("bookId", String.valueOf(book.getId())).param("readDate", "2026-09-21")
            .param("memo", "面白かった"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/bookReadingLog/list"));

    entityManager.flush();
    entityManager.clear();

    BookReadingLogEntity saved = bookReadingLogRepository.findAll().getLast();
    assertNotNull(saved);
    assertEquals(book.getId(), saved.getBook().getId());
    assertEquals(LocalDate.of(2026, 9, 21), saved.getReadDate());
    assertEquals("面白かった", saved.getMemo());
  }
}
