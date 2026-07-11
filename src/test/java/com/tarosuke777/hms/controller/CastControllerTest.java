package com.tarosuke777.hms.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tarosuke777.hms.entity.CastEntity;
import com.tarosuke777.hms.form.CastForm;
import com.tarosuke777.hms.mapper.CastMapper;
import com.tarosuke777.hms.repository.CastRepository;
import com.tarosuke777.hms.security.LoginUser;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.TestSecurityContextHolder;
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
public class CastControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CastRepository castRepository;

  @Autowired
  private CastMapper castMapper;

  @Autowired
  private EntityManager entityManager;

  private static final String LIST_ENDPOINT = "/cast/list";
  private static final String LIST_VIEW = "cast/list";
  private static final String LIST_URL = "/cast/list";

  private static final String DETAIL_ENDPOINT = "/cast/detail/{id}";
  private static final String DETAIL_VIEW = "cast/detail";

  private static final String REGISTER_ENDPOINT = "/cast/register";
  private static final String REGISTER_VIEW = "cast/register";

  private static final String UPDATE_ENDPOINT = "/cast/detail";
  private static final String DELETE_ENDPOINT = "/cast/detail";

  @Test
  void getList_ShouldReturnCastList() throws Exception {
    // Given
    LoginUser loginUser =
        (LoginUser) TestSecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer currentUserId = loginUser.getId();
    List<CastForm> expectedCastList =
        castRepository.findByCreatedBy(currentUserId).stream().map(castMapper::toForm).toList();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("castList", expectedCastList))
        .andExpect(view().name(LIST_VIEW));
  }

  @Test
  void getDetail_ShouldReturnCastDetail() throws Exception {
    // Given
    CastEntity entity = castRepository.findAll().get(0);
    CastForm expectedCastForm = castMapper.toForm(entity);

    // When & Then
    performGetDetailRequest(entity.getId()).andExpect(status().isOk())
        .andExpect(model().attribute("castForm", expectedCastForm))
        .andExpect(view().name(DETAIL_VIEW)).andExpect(model().hasNoErrors());
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
    String name = "Test Cast Name";

    // When & Then
    performRegisterRequest(name).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    List<CastEntity> castList = castRepository.findAll();
    CastEntity savedCast = castList.get(castList.size() - 1);
    assertEquals(name, savedCast.getName());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {
    // Given
    CastEntity targetCast = castRepository.findAll().get(0);
    CastForm form = castMapper.toForm(targetCast);
    form.setName("更新後のキャスト名");

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    CastEntity updatedCast = castRepository.findById(form.getId()).orElse(null);
    assertNotNull(updatedCast);
    assertEquals(form.getName(), updatedCast.getName());
  }

  @Test
  void delete_ExistingCast_ShouldDeleteAndRedirectToList() throws Exception {
    // Given
    CastEntity targetCast = castRepository.findAll().get(0);

    // When & Then
    performDeleteRequest(targetCast.getId()).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    CastEntity deletedCast = castRepository.findById(targetCast.getId()).orElse(null);
    assertNull(deletedCast);
  }

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int id) throws Exception {
    return mockMvc
        .perform(get(DETAIL_ENDPOINT, id).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(String name) throws Exception {
    return mockMvc.perform(post(REGISTER_ENDPOINT).with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("name", name)).andDo(print());
  }

  private ResultActions performUpdateRequest(CastForm form) throws Exception {
    return mockMvc.perform(
        post(UPDATE_ENDPOINT).with(csrf()).param("update", "").param("id", form.getId().toString())
            .param("name", form.getName()).param("version", form.getVersion().toString()))
        .andDo(print());
  }

  private ResultActions performDeleteRequest(int id) throws Exception {
    return mockMvc
        .perform(
            post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("id", String.valueOf(id)))
        .andDo(print());
  }
}
