package arpa.home.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import arpa.home.hms.form.GameVideoForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
        .andExpect(content().string(org.hamcrest.Matchers
            .containsString("http://192.168.10.199:8080/videos/game%20sample.mp4")))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("ゲーム動画")));
  }

  @Test
  void getList_FiltersByTitleAndNameAndPreservesFiltersInPagination() throws Exception {
    MvcResult result = mockMvc
        .perform(get("/gameVideo/list").param("title", "Test Game A")
            .param("name", "Alternate recording").param("size", "1"))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("title=Test%20Game%20A")))
        .andExpect(
            content().string(org.hamcrest.Matchers.containsString("name=Alternate%20recording")))
        .andReturn();

    Object pageAttribute = result.getModelAndView().getModel().get("gameVideoPage");
    org.junit.jupiter.api.Assertions.assertInstanceOf(Page.class, pageAttribute);
    Page<?> page = (Page<?>) pageAttribute;
    org.assertj.core.api.Assertions.assertThat(page.getContent())
        .extracting(video -> ((GameVideoForm) video).name()).containsExactly("Alternate recording");
  }

  @Test
  void getNames_ReturnsOnlyNamesForSelectedTitle() throws Exception {
    mockMvc.perform(get("/gameVideo/names").param("title", "Test Game A"))
        .andExpect(status().isOk()).andExpect(jsonPath("$[0]").value("Alternate recording"))
        .andExpect(jsonPath("$[1]").value("First recording"))
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void getNames_WhenTitleHasNoNamedVideos_ReturnsEmptyList() throws Exception {
    mockMvc.perform(get("/gameVideo/names").param("title", "Test Game B"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getDetail_RendersVideoAndMetadata() throws Exception {
    mockMvc.perform(get("/gameVideo/detail/{id}", 2L)).andExpect(status().isOk())
        .andExpect(view().name("gameVideo/detail"))
        .andExpect(MockMvcResultMatchers.model().attributeExists("gameVideo"))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Game B")))
        .andExpect(content().string(
            org.hamcrest.Matchers.containsString("http://192.168.10.199:8080/videos/second.mp4")))
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
