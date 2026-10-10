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

import arpa.home.hms.entity.CodingLogEntity;
import arpa.home.hms.form.CodingLogForm;
import arpa.home.hms.repository.CodingLogRepository;
import arpa.home.hms.repository.UserRepository;
import arpa.home.hms.service.CodingLogService;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
class CodingLogControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CodingLogRepository codingLogRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CodingLogService codingLogService;

  @Autowired
  private EntityManager entityManager;

  @Test
  void getList_ShouldShowOnlyCurrentUsersLogs() throws Exception {
    mockMvc.perform(get("/codingLog/list")).andExpect(status().isOk())
        .andExpect(model().attributeExists("codingLogPage"))
        .andExpect(content().string(
            org.hamcrest.Matchers.containsString("https://atcoder.jp/contests/123/tasks/abc123")))
        .andExpect(content()
            .string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("other-user"))))
        .andExpect(view().name("codingLog/list"));
  }

  @Test
  void getRegister_ShouldSetTodayAsDefaultSolvedDate() throws Exception {
    mockMvc.perform(get("/codingLog/register")).andExpect(status().isOk())
        .andExpect(model().attribute("codingLogForm",
            org.hamcrest.Matchers.hasProperty("solvedDate",
                org.hamcrest.Matchers.equalTo(LocalDate.now(ZoneId.systemDefault())))))
        .andExpect(view().name("codingLog/register"));
  }

  @Test
  void register_WithValidData_ShouldPersistCodingLog() throws Exception {
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();

    mockMvc
        .perform(post("/codingLog/register").with(csrf()).param("solvedDate", "2026-10-08")
            .param("problemUrl", "https://leetcode.com/problems/two-sum/")
            .param("memo", "Hash map solution"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/codingLog/list"));

    entityManager.flush();
    entityManager.clear();

    CodingLogEntity saved = codingLogRepository
        .findByCreatedByOrderBySolvedDateDescIdDesc(adminId,
            org.springframework.data.domain.PageRequest.of(0, 10))
        .getContent().stream()
        .filter(log -> "https://leetcode.com/problems/two-sum/".equals(log.getProblemUrl()))
        .findFirst().orElseThrow();
    assertEquals(LocalDate.of(2026, 10, 8), saved.getSolvedDate());
    assertEquals("https://leetcode.com/problems/two-sum/", saved.getProblemUrl());
    assertEquals("Hash map solution", saved.getMemo());
  }

  @Test
  void register_WithInvalidUrl_ShouldReturnFormErrors() throws Exception {
    mockMvc
        .perform(post("/codingLog/register").with(csrf()).param("solvedDate", "2026-10-08")
            .param("problemUrl", "not-a-url").param("memo", "invalid"))
        .andExpect(status().isOk())
        .andExpect(model().attributeHasFieldErrors("codingLogForm", "problemUrl"))
        .andExpect(view().name("codingLog/register"));

    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    assertEquals(1, codingLogRepository.findByCreatedByOrderBySolvedDateDescIdDesc(adminId,
        org.springframework.data.domain.PageRequest.of(0, 10)).getTotalElements());
  }

  @Test
  void detail_UpdateThenDelete_ShouldChangeOnlyOwnedLog() throws Exception {
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    CodingLogEntity log = codingLogRepository.findByCreatedByOrderBySolvedDateDescIdDesc(adminId,
        org.springframework.data.domain.PageRequest.of(0, 10)).getContent().getFirst();

    mockMvc.perform(get("/codingLog/detail/" + log.getId())).andExpect(status().isOk())
        .andExpect(model().attributeExists("codingLogForm"))
        .andExpect(view().name("codingLog/detail"));

    mockMvc
        .perform(post("/codingLog/detail").with(csrf()).param("update", "")
            .param("id", String.valueOf(log.getId())).param("version", "0")
            .param("solvedDate", "2026-10-07")
            .param("problemUrl", "https://leetcode.com/problems/two-sum/").param("memo", "Updated"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/codingLog/list"));

    entityManager.flush();
    entityManager.clear();
    CodingLogEntity updated = codingLogRepository.findById(log.getId()).orElseThrow();
    assertEquals("Updated", updated.getMemo());
    assertEquals(LocalDate.of(2026, 10, 7), updated.getSolvedDate());

    mockMvc
        .perform(post("/codingLog/detail").with(csrf()).param("delete", "").param("id",
            String.valueOf(log.getId())))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/codingLog/list"));

    assertFalse(codingLogRepository.existsByIdAndCreatedBy(log.getId(), adminId));
  }

  @Test
  void service_ShouldRejectAnotherUsersLogForReadUpdateAndDelete() {
    Integer adminId = userRepository.findByName("admin").orElseThrow().getId();
    Integer otherUserId = userRepository.findByName("other-user").orElseThrow().getId();
    CodingLogEntity otherUsersLog =
        codingLogRepository.findByCreatedByOrderBySolvedDateDescIdDesc(otherUserId,
            org.springframework.data.domain.PageRequest.of(0, 10)).getContent().getFirst();

    assertThrows(RuntimeException.class,
        () -> codingLogService.getCodingLogDetails(otherUsersLog.getId(), adminId));
    CodingLogForm form = new CodingLogForm();
    form.setId(otherUsersLog.getId());
    assertThrows(RuntimeException.class, () -> codingLogService.updateCodingLog(form, adminId));
    assertThrows(RuntimeException.class,
        () -> codingLogService.deleteCodingLog(otherUsersLog.getId(), adminId));
    assertTrue(codingLogRepository.existsByIdAndCreatedBy(otherUsersLog.getId(), otherUserId));
  }
}
