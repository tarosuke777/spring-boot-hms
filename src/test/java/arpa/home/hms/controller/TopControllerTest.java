package arpa.home.hms.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
class TopControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getTop_ShouldDisplayLatestBookReadingLogs() throws Exception {
    mockMvc.perform(get("/top")).andExpect(status().isOk())
        .andExpect(model().attribute("latestBookReadingLogs", org.hamcrest.Matchers.hasSize(5)))
        .andExpect(model().attributeExists("bookMap"))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("直近の読書履歴")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("newest")))
        .andExpect(content()
            .string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("oldest"))))
        .andExpect(content()
            .string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("other-user"))))
        .andExpect(result -> {
          String html = result.getResponse().getContentAsString();
          assertTrue(html.indexOf("same-day-later") < html.indexOf("same-day-earlier"));
          assertFalse(html.contains("oldest"));
        }).andExpect(content().string(org.hamcrest.Matchers.containsString("/bookReadingLog/list")))
        .andExpect(view().name("top"));
  }
}
