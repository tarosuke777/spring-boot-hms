package arpa.home.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithMockUser(roles = "ADMIN")
class GameVideoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getList_RendersPageAndVideoPreviewWithPagination() throws Exception {
    mockMvc.perform(get("/gameVideo/list").param("size", "1")).andExpect(status().isOk())
        .andExpect(view().name("gameVideo/list"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("gameVideoPage"))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Game C")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("<video")))
        .andExpect(content().string(
            org.hamcrest.Matchers.containsString("https://hv.home.arpa/videos/game%20sample.mp4")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("ゲーム動画")));
  }

  @Test
  void getDetail_RendersVideoAndMetadata() throws Exception {
    mockMvc.perform(get("/gameVideo/detail/{id}", 2L)).andExpect(status().isOk())
        .andExpect(view().name("gameVideo/detail"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("gameVideo"))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Game B")))
        .andExpect(content()
            .string(org.hamcrest.Matchers.containsString("https://hv.home.arpa/videos/second.mp4")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("<video")));
  }

  @Test
  void getDetail_WhenVideoDoesNotExist_ReturnsNotFound() throws Exception {
    mockMvc.perform(get("/gameVideo/detail/{id}", 999L)).andExpect(status().isNotFound());
  }

  @Test
  void postToGameVideoRoute_IsNotSupported() throws Exception {
    mockMvc.perform(post("/gameVideo/detail/1").with(csrf()))
        .andExpect(status().isMethodNotAllowed());
  }
}
