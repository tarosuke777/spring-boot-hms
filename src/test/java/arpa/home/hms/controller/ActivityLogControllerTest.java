package arpa.home.hms.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import arpa.home.hms.entity.ActivityLogEntity;
import arpa.home.hms.form.ActivityLogForm;
import arpa.home.hms.repository.ActivityLogRepository;
import arpa.home.hms.repository.BookRepository;
import arpa.home.hms.repository.MovieRepository;
import arpa.home.hms.repository.UserRepository;
import arpa.home.hms.service.ActivityLogService;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
class ActivityLogControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ActivityLogRepository activityLogRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private MovieRepository movieRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ActivityLogService activityLogService;

  @Autowired
  private EntityManager entityManager;

  @Test
  void getList_ShouldShowOnlyCurrentUsersLogs() throws Exception {
    mockMvc.perform(get("/activityLog/list")).andExpect(status().isOk())
        .andExpect(model().attributeExists("activityLogPage"))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("activity-newest")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Activity Book")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Activity Video")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("/book/detail/")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("/movie/detail/")))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("other-activity-user"))))
        .andExpect(view().name("activityLog/list"));
  }

  @Test
  void getRegister_ShouldSetTodayAsDefaultActivityDate() throws Exception {
    mockMvc.perform(get("/activityLog/register")).andExpect(status().isOk())
        .andExpect(model().attribute("activityLogForm",
            org.hamcrest.Matchers.hasProperty("activityDate",
                org.hamcrest.Matchers.equalTo(LocalDate.now(ZoneId.systemDefault())))))
        .andExpect(content().string(org.hamcrest.Matchers
            .containsString("value=\"" + LocalDate.now(ZoneId.systemDefault()) + "\"")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("書籍")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("動画")))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Book")))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Video")))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Book"))))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Video"))))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("id=\"bookTargetId\"")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("id=\"videoTargetId\"")))
        .andExpect(view().name("activityLog/register"));
  }

  @Test
  void getRegister_ShouldOnlyOfferCurrentUsersTitles() throws Exception {
    mockMvc.perform(get("/activityLog/register")).andExpect(status().isOk())
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Book")))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Video")))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Other User Book"))))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Other User Video"))))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Book"))))
        .andExpect(content().string(org.hamcrest.Matchers
            .not(org.hamcrest.Matchers.containsString("Test Activity Video"))));
  }

  @Test
  void register_WithValidData_ShouldPersistActivityLog() throws Exception {
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    Integer bookId =
        bookRepository.findByCreatedByAndIsAdultTrueOrderByIdAsc(adminId).getFirst().getId();

    mockMvc
        .perform(post("/activityLog/register").with(csrf()).param("activityDate", "2026-10-08")
            .param("targetType", "BOOK").param("targetId", String.valueOf(bookId))
            .param("memo", "Daily note"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/activityLog/list"));

    entityManager.flush();
    entityManager.clear();

    ActivityLogEntity saved = activityLogRepository
        .findByCreatedByOrderByActivityDateDescIdDesc(adminId,
            org.springframework.data.domain.PageRequest.of(0, 10))
        .getContent().stream().filter(log -> "Daily note".equals(log.getMemo())).findFirst()
        .orElseThrow();
    assertEquals(LocalDate.of(2026, 10, 8), saved.getActivityDate());
    assertEquals("BOOK", saved.getTargetType());
    assertEquals(bookId, saved.getTargetId());
    assertEquals("Daily note", saved.getMemo());
  }

  @Test
  void register_WithAnotherUsersTitle_ShouldReturnTargetIdError() throws Exception {
    Integer otherUserId = userRepository.findByName("user").orElseThrow().getId();
    Integer otherBookId =
        bookRepository.findByCreatedByOrderByIdAsc(otherUserId).getFirst().getId();
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();

    mockMvc
        .perform(post("/activityLog/register").with(csrf()).param("activityDate", "2026-10-08")
            .param("targetType", "BOOK").param("targetId", String.valueOf(otherBookId))
            .param("memo", "foreign book"))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("activityLogForm", "targetId"))
        .andExpect(view().name("activityLog/register"));

    assertEquals(7, activityLogRepository.findByCreatedByOrderByActivityDateDescIdDesc(adminId,
        org.springframework.data.domain.PageRequest.of(0, 10)).getTotalElements());
  }

  @Test
  void register_WithInvalidTargetType_ShouldReturnFormErrors() throws Exception {
    mockMvc
        .perform(post("/activityLog/register").with(csrf()).param("activityDate", "2026-10-08")
            .param("targetType", "").param("targetId", "1").param("memo", "invalid"))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("activityLogForm", "targetType"))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Book")))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Video")))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Book"))))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Video"))))
        .andExpect(view().name("activityLog/register"));

    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    assertEquals(7, activityLogRepository.findByCreatedByOrderByActivityDateDescIdDesc(adminId,
        org.springframework.data.domain.PageRequest.of(0, 10)).getTotalElements());
  }

  @Test
  void register_WithUnsupportedTargetType_ShouldReturnFormErrors() throws Exception {
    mockMvc
        .perform(post("/activityLog/register").with(csrf()).param("activityDate", "2026-10-08")
            .param("targetType", "UNSUPPORTED").param("targetId", "1").param("memo", "invalid"))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("activityLogForm", "targetType"))
        .andExpect(view().name("activityLog/register"));
  }

  @Test
  void detail_UpdateThenDelete_ShouldChangeOnlyOwnedLog() throws Exception {
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    ActivityLogEntity log =
        activityLogRepository.findByCreatedByOrderByActivityDateDescIdDesc(adminId,
            org.springframework.data.domain.PageRequest.of(0, 10)).getContent().getFirst();
    Integer videoId =
        movieRepository.findByCreatedByAndIsAdultTrueOrderByIdAsc(adminId).getFirst().getId();

    mockMvc.perform(get("/activityLog/detail/" + log.getId())).andExpect(status().isOk())
        .andExpect(model().attributeExists("activityLogForm"))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Book")))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Video")))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Book"))))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Video"))))
        .andExpect(view().name("activityLog/detail"));

    mockMvc
        .perform(post("/activityLog/detail").with(csrf()).param("update", "")
            .param("id", String.valueOf(log.getId())).param("version", "0")
            .param("activityDate", "2026-10-07").param("targetType", "VIDEO")
            .param("targetId", String.valueOf(videoId)).param("memo", "Updated"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/activityLog/list"));

    entityManager.flush();
    entityManager.clear();
    ActivityLogEntity updated = activityLogRepository.findById(log.getId()).orElseThrow();
    assertEquals("Updated", updated.getMemo());
    assertEquals(LocalDate.of(2026, 10, 7), updated.getActivityDate());
    assertEquals("VIDEO", updated.getTargetType());
    assertEquals(videoId, updated.getTargetId());

    mockMvc
        .perform(post("/activityLog/detail").with(csrf()).param("delete", "").param("id",
            String.valueOf(log.getId())))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/activityLog/list"));

    assertFalse(activityLogRepository.existsByIdAndCreatedBy(log.getId(), adminId));
  }

  @Test
  void detail_UpdateWithAnotherUsersTitle_ShouldReturnTargetIdError() throws Exception {
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    Integer otherUserId = userRepository.findByName("user").orElseThrow().getId();
    ActivityLogEntity log =
        activityLogRepository.findByCreatedByOrderByActivityDateDescIdDesc(adminId,
            org.springframework.data.domain.PageRequest.of(0, 10)).getContent().getFirst();
    Integer otherMovieId = movieRepository.findByCreatedBy(otherUserId).getFirst().getId();

    mockMvc
        .perform(post("/activityLog/detail").with(csrf()).param("update", "")
            .param("id", String.valueOf(log.getId())).param("version", "0")
            .param("activityDate", "2026-10-07").param("targetType", "VIDEO")
            .param("targetId", String.valueOf(otherMovieId)).param("memo", "foreign video"))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("activityLogForm", "targetId"))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Book")))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Video")))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Book"))))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Video"))))
        .andExpect(view().name("activityLog/detail"));

    assertFalse(activityLogRepository.findByIdAndCreatedBy(log.getId(), adminId).orElseThrow()
        .getMemo().equals("foreign video"));
  }

  @Test
  void detail_LegacyMovieTarget_ShouldNormalizeAndAllowUpdate() throws Exception {
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    ActivityLogEntity legacyMovieLog = activityLogRepository
        .findByCreatedByOrderByActivityDateDescIdDesc(adminId,
            org.springframework.data.domain.PageRequest.of(0, 10))
        .getContent().stream().filter(log -> "activity-oldest".equals(log.getMemo())).findFirst()
        .orElseThrow();

    mockMvc.perform(get("/activityLog/detail/" + legacyMovieLog.getId())).andExpect(status().isOk())
        .andExpect(model().attribute("activityLogForm",
            org.hamcrest.Matchers.hasProperty("targetType",
                org.hamcrest.Matchers.equalTo("VIDEO"))))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("Test Adult Activity Video")))
        .andExpect(content().string(
            org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test Activity Video"))))
        .andExpect(view().name("activityLog/detail"));

    mockMvc
        .perform(post("/activityLog/detail").with(csrf()).param("update", "")
            .param("id", String.valueOf(legacyMovieLog.getId())).param("version", "0")
            .param("activityDate", "2026-09-20").param("targetType", "VIDEO")
            .param("targetId", String.valueOf(legacyMovieLog.getTargetId()))
            .param("memo", "activity-oldest"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/activityLog/list"));

    entityManager.flush();
    entityManager.clear();
    assertEquals("VIDEO",
        activityLogRepository.findById(legacyMovieLog.getId()).orElseThrow().getTargetType());
  }

  @Test
  void service_ShouldRejectAnotherUsersLogForReadUpdateAndDelete() {
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    Integer otherUserId = userRepository.findByName("user").orElseThrow().getId();
    ActivityLogEntity otherUsersLog =
        activityLogRepository.findByCreatedByOrderByActivityDateDescIdDesc(otherUserId,
            org.springframework.data.domain.PageRequest.of(0, 10)).getContent().getFirst();

    assertThrows(RuntimeException.class,
        () -> activityLogService.getActivityLogDetails(otherUsersLog.getId(), adminId));
    ActivityLogForm form = new ActivityLogForm();
    form.setId(otherUsersLog.getId());
    assertThrows(RuntimeException.class, () -> activityLogService.updateActivityLog(form, adminId));
    assertThrows(RuntimeException.class,
        () -> activityLogService.deleteActivityLog(otherUsersLog.getId(), adminId));
    assertTrue(activityLogRepository.existsByIdAndCreatedBy(otherUsersLog.getId(), otherUserId));
  }
}
