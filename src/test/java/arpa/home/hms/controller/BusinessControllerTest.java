package arpa.home.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import arpa.home.hms.entity.BusinessEntity;
import arpa.home.hms.enums.BusinessGenre;
import arpa.home.hms.enums.BusinessStatus;
import arpa.home.hms.form.BusinessForm;
import arpa.home.hms.mapper.BusinessMapper;
import arpa.home.hms.repository.BusinessRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class BusinessControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private BusinessRepository businessRepository;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private BusinessMapper businessMapper;

  @Test
  void getList_ShouldReturnBusinessPage() throws Exception {
    mockMvc.perform(get("/business/list")).andDo(print()).andExpect(status().isOk())
        .andExpect(view().name("business/list")).andExpect(model().attributeExists("businessPage"));
  }

  @Test
  void getRegister_ShouldReturnRegisterPage() throws Exception {
    mockMvc.perform(get("/business/register").accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk()).andExpect(view().name("business/register"));
  }

  @Test
  void register_WithValidData_ShouldSaveBusinessAndCanvas() throws Exception {
    mockMvc.perform(post("/business/register").with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", "テスト事業")
        .param("genre", BusinessGenre.SAAS.name()).param("status", BusinessStatus.IDEA.name())
        .param("overview", "テスト概要").param("customerSegments", "B2B").param("valueProposition", "価値")
        .param("channels", "Web").param("customerRelationships", "メール")
        .param("revenueStreams", "月額").param("keyResources", "開発者").param("keyActivities", "開発")
        .param("keyPartners", "提携先").param("costStructure", "人件費"))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/business/list"));

    entityManager.flush();
    entityManager.clear();

    BusinessEntity saved = businessRepository.findAll().stream()
        .filter(item -> "テスト事業".equals(item.getName())).findFirst().orElseThrow();
    Assertions.assertNotNull(saved.getCanvas());
    Assertions.assertEquals("B2B", saved.getCanvas().getCustomerSegments());
  }

  @Test
  void getDetail_ShouldReturnBusinessForm() throws Exception {
    BusinessEntity entity = businessRepository.findAll().stream().findFirst().orElseThrow();
    BusinessForm expected = businessMapper.toForm(entity);

    MvcResult result =
        mockMvc.perform(get("/business/detail/{id}", entity.getId()).accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk()).andExpect(view().name("business/detail"))
            .andExpect(model().attribute("businessForm", expected)).andReturn();

    String html = result.getResponse().getContentAsString();
    Assertions.assertTrue(html.contains("grid-template-columns: repeat(5"));
    Assertions.assertTrue(html.contains("\"partners activities value relationships segments\""));
    Assertions.assertTrue(html.contains("@media (max-width: 992px)"));
    Assertions.assertTrue(html.contains("flex: 1"));
    for (String field : List.of("keyPartners", "keyActivities", "keyResources", "valueProposition",
        "customerRelationships", "channels", "customerSegments", "costStructure",
        "revenueStreams")) {
      Assertions.assertTrue(html.contains("name=\"" + field + "\""), field + " binding missing");
    }
  }

  @Test
  void update_WithValidData_ShouldPersistNewValues() throws Exception {
    BusinessEntity entity = businessRepository.findAll().stream().findFirst().orElseThrow();
    BusinessForm form = businessMapper.toForm(entity);
    form.setName("更新後の事業名");
    form.setOverview("更新後の概要");
    form.setCustomerSegments("更新後の顧客セグメント");

    mockMvc.perform(post("/business/detail").with(csrf()).param("update", "")
        .param("id", String.valueOf(form.getId())).param("name", form.getName())
        .param("genre", String.valueOf(form.getGenre()))
        .param("status", String.valueOf(form.getStatus())).param("overview", form.getOverview())
        .param("customerSegments", form.getCustomerSegments())
        .param("valueProposition", form.getValueProposition()).param("channels", form.getChannels())
        .param("customerRelationships", form.getCustomerRelationships())
        .param("revenueStreams", form.getRevenueStreams())
        .param("keyResources", form.getKeyResources())
        .param("keyActivities", form.getKeyActivities()).param("keyPartners", form.getKeyPartners())
        .param("costStructure", form.getCostStructure())
        .param("version", String.valueOf(form.getVersion()))).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/business/detail/" + entity.getId()));

    entityManager.flush();
    entityManager.clear();

    BusinessEntity saved = businessRepository.findById(entity.getId()).orElseThrow();
    Assertions.assertEquals("更新後の事業名", saved.getName());
    Assertions.assertEquals("更新後の顧客セグメント", saved.getCanvas().getCustomerSegments());
  }

  @Test
  void delete_ExistingBusiness_ShouldDeleteAndRedirectToList() throws Exception {
    BusinessEntity entity = businessRepository.findAll().stream().findFirst().orElseThrow();

    mockMvc
        .perform(post("/business/detail").with(csrf()).param("delete", "").param("id",
            String.valueOf(entity.getId())))
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/business/list"));

    entityManager.flush();
    entityManager.clear();

    Assertions.assertFalse(businessRepository.findById(entity.getId()).isPresent());
  }
}
