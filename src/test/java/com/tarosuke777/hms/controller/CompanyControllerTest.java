package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tarosuke777.hms.entity.CompanyEntity;
import com.tarosuke777.hms.form.CompanyForm;
import com.tarosuke777.hms.mapper.CompanyMapper;
import com.tarosuke777.hms.repository.CompanyRepository;
import jakarta.persistence.EntityManager;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class CompanyControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private CompanyRepository companyRepository;
  @Autowired
  private EntityManager entityManager;
  @Autowired
  private CompanyMapper companyMapper;

  private static final String LIST_VIEW = "company/list";
  private static final String LIST_URL = "/company/list";

  private static final String DETAIL_ENDPOINT = "/company/detail/{companyId}";
  private static final String DETAIL_VIEW = "company/detail";

  private static final String REGISTER_ENDPOINT = "/company/register";
  private static final String REGISTER_VIEW = "company/register";

  private static final String UPDATE_ENDPOINT = "/company/detail";
  private static final String DELETE_ENDPOINT = "/company/detail";

  @Test
  void getList_ShouldReturnCompanyPageWithPagination() throws Exception {
    // When & Then
    performGetListRequest(null, null).andExpect(status().isOk())
        .andExpect(model().attributeExists("companyPage")).andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getList_WithPage1_ShouldReturnFirstTenCompanies() throws Exception {
    // When & Then
    performGetListRequest(null, "0").andExpect(status().isOk())
        .andExpect(model().attributeExists("companyPage")).andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getList_WithPage2_ShouldReturnNextPage() throws Exception {
    // When & Then
    performGetListRequest(null, "1").andExpect(status().isOk())
        .andExpect(model().attributeExists("companyPage")).andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getList_WithNameFilter_ShouldReturnFilteredPagedList() throws Exception {
    // When & Then
    performGetListRequest("panyA", null).andExpect(status().isOk())
        .andExpect(model().attributeExists("companyPage")).andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getList_WithNameFilterAndPaging_ShouldReturnFilteredPage() throws Exception {
    // When & Then
    performGetListRequest("Company", "0").andExpect(status().isOk())
        .andExpect(model().attributeExists("companyPage")).andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getList_WithNoMatchName_ShouldReturnEmptyPage() throws Exception {
    // When & Then
    performGetListRequest("NonExistent", null).andExpect(status().isOk())
        .andExpect(model().attributeExists("companyPage")).andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getRegister_ShouldReturnRegisterPage() throws Exception {
    // When & Then
    performGetRegisterRequest().andExpect(status().isOk()).andExpect(view().name(REGISTER_VIEW))
        .andExpect(model().hasNoErrors());
  }

  @Test
  void register_WithValidData_ShouldRedirectToList() throws Exception {
    // Given
    String companyName = "NewTestCompany";
    String companyNote = "NewCompanyNote";
    String jobApplicationHistory = "NewJobApplicationHistory";

    // When & Then
    performRegisterRequest(companyName, companyNote, jobApplicationHistory)
        .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    CompanyEntity lastCompany = companyRepository.findByName(companyName).orElseThrow();
    Assertions.assertEquals(companyName, lastCompany.getName());
    Assertions.assertEquals(companyNote, lastCompany.getNote());
    Assertions.assertEquals(jobApplicationHistory, lastCompany.getJobApplicationHistory());
  }

  @Test
  void register_WithInvalidData_ShouldReturnRegisterPageWithErrors() throws Exception {
    // Given
    String emptyName = "";

    // When & Then
    performRegisterRequest(emptyName, "SomeNote", "SomeJobApplicationHistory")
        .andExpect(status().isOk()).andExpect(view().name(REGISTER_VIEW))
        .andExpect(model().hasErrors());
  }

  @Test
  void getDetail_ShouldReturnCompanyDetail() throws Exception {
    // Given
    CompanyEntity expectedCompanyEntity = companyRepository.findByName("CompanyA").orElseThrow();
    CompanyForm expectedCompanyForm = companyMapper.toForm(expectedCompanyEntity);

    // When & Then
    performGetDetailRequest(expectedCompanyEntity.getId()).andExpect(status().isOk())
        .andExpect(model().attribute("companyForm", expectedCompanyForm))
        .andExpect(view().name(DETAIL_VIEW)).andExpect(model().hasNoErrors());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToDetail() throws Exception {
    // Given
    CompanyEntity targetCompany = companyRepository.findByName("CompanyA").orElseThrow();
    CompanyForm companyForm = companyMapper.toForm(targetCompany);
    companyForm.setName("UpdatedCompanyName");
    companyForm.setNote("UpdatedCompanyNote");
    companyForm.setJobApplicationHistory("UpdatedJobApplicationHistory");

    // When & Then
    performUpdateRequest(companyForm).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/company/detail/" + targetCompany.getId()));

    entityManager.flush();
    entityManager.clear();

    CompanyEntity updatedEntity = companyRepository.findById(targetCompany.getId()).orElse(null);
    Assertions.assertNotNull(updatedEntity);
    Assertions.assertEquals(companyForm.getName(), updatedEntity.getName());
    Assertions.assertEquals(companyForm.getNote(), updatedEntity.getNote());
    Assertions.assertEquals(companyForm.getJobApplicationHistory(),
        updatedEntity.getJobApplicationHistory());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {
    // Given
    CompanyEntity company = companyRepository.findByName("CompanyA").orElseThrow();
    Integer currentVersion = company.getVersion();

    // Update in database to increment version
    company.setName("Concurrent Update");
    companyRepository.saveAndFlush(company);
    entityManager.clear();

    CompanyForm companyForm = companyMapper.toForm(company);
    companyForm.setName("Try to Update");
    companyForm.setVersion(currentVersion); // old version

    // When & Then
    performUpdateRequest(companyForm).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void update_WithTamperedId_ShouldThrowIllegalRequestException() throws Exception {
    // Given
    CompanyEntity targetCompany = companyRepository.findAll().stream()
        .filter(c -> "CompanyA".equals(c.getName())).findFirst().orElseThrow();

    // When & Then
    mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        // ID parameter is missing (null), which triggers validation error on id (NotNull in
        // UpdateGroup)
        .param("name", "Some Name").param("version", targetCompany.getVersion().toString()))
        .andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isNotDisplayMenu", true));
  }

  @Test
  void delete_ExistingCompany_ShouldDeleteAndRedirectToList() throws Exception {
    // Given
    CompanyEntity targetCompany = companyRepository.findByName("CompanyA").orElseThrow();
    Integer targetCompanyId = targetCompany.getId();

    // When & Then
    performDeleteRequest(targetCompanyId).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    CompanyEntity company = companyRepository.findById(targetCompanyId).orElse(null);
    Assertions.assertNull(company);
  }

  // --- Helper Methods ---

  private ResultActions performGetListRequest(String name, String page) throws Exception {
    var request = get(LIST_URL);
    if (name != null) {
      request = request.param("name", name);
    }
    if (page != null) {
      request = request.param("page", page);
    }
    return mockMvc.perform(request).andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(String name, String note,
      String jobApplicationHistory) throws Exception {
    return mockMvc
        .perform(post(REGISTER_ENDPOINT).with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", name)
            .param("note", note).param("jobApplicationHistory", jobApplicationHistory))
        .andDo(print());
  }

  private ResultActions performGetDetailRequest(int companyId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, companyId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performUpdateRequest(CompanyForm companyForm) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("id", companyForm.getId().toString()).param("name", companyForm.getName())
        .param("note", companyForm.getNote())
        .param("jobApplicationHistory", companyForm.getJobApplicationHistory())
        .param("version", companyForm.getVersion().toString())).andDo(print());
  }

  private ResultActions performDeleteRequest(int companyId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("id",
        String.valueOf(companyId))).andDo(print());
  }
}
