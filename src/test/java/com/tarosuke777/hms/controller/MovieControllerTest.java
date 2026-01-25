package com.tarosuke777.hms.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
import com.tarosuke777.hms.entity.MovieEntity;
import com.tarosuke777.hms.form.MovieForm;
import com.tarosuke777.hms.repository.MovieRepository;
import jakarta.persistence.EntityManager;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class MovieControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private MovieRepository movieRepository; // Repositoryへ変更
  @Autowired
  private ModelMapper modelMapper; // ModelMapper追加
  @Autowired
  private EntityManager entityManager; // キャッシュクリア用

  private static final String LIST_ENDPOINT = "/movie/list";
  private static final String LIST_VIEW = "movie/list";
  private static final String LIST_URL = "/movie/list";

  private static final String DETAIL_ENDPOINT = "/movie/detail/{id}";
  private static final String DETAIL_VIEW = "movie/detail";

  private static final String REGISTER_ENDPOINT = "/movie/register";
  private static final String REGISTER_VIEW = "movie/register";

  private static final String UPDATE_ENDPOINT = "/movie/detail";
  private static final String DELETE_ENDPOINT = "/movie/detail";

  @Test
  void getList_ShouldReturnMovieList() throws Exception {

    // Given
    List<MovieForm> expectedMovieList = movieRepository.findByCreatedBy("admin").stream()
        .map(entity -> modelMapper.map(entity, MovieForm.class)).toList();

    // When & Then
    performGetListRequest().andExpect(status().isOk())
        .andExpect(model().attribute("movieList", expectedMovieList))
        .andExpect(view().name(LIST_VIEW));
  }


  @Test
  void getDetail_ShouldReturnMovieDetail() throws Exception {

    // Given
    MovieEntity entity = movieRepository.findAll().getFirst();
    MovieForm expectedMovieForm = modelMapper.map(entity, MovieForm.class);

    // When & Then
    performGetDetailRequest(entity.getMovieId()).andExpect(status().isOk())
        .andExpect(model().attribute("movieForm", expectedMovieForm))
        .andExpect(view().name(DETAIL_VIEW)).andExpect(model().hasNoErrors());
  }


  @Test
  void getRegister_ShouldReturnRegisterPage() throws Exception {

    // Given

    // When & Then
    performGetRegisterRequest().andExpect(status().isOk()).andExpect(view().name(REGISTER_VIEW))
        .andExpect(model().hasNoErrors());
  }


  @Test
  void register_WithValidData_ShouldRedirectToList() throws Exception {

    // Given
    String movieName = "TestMovieName";

    // When & Then
    performRegisterRequest(movieName).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    MovieEntity lastMovie = movieRepository.findAll().getLast();
    Assertions.assertEquals(movieName, lastMovie.getMovieName());
  }

  @Test
  void update_WithValidData_ShouldUpdateAndRedirectToList() throws Exception {

    // Given
    MovieEntity movie = movieRepository.findAll().getFirst();
    MovieForm form = modelMapper.map(movie, MovieForm.class);
    form.setMovieName("著者２");

    // When & Then
    performUpdateRequest(form).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    TestSecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
    entityManager.flush();
    entityManager.clear();

    MovieEntity updatedMovie = movieRepository.findById(form.getMovieId()).orElse(null);
    Assertions.assertEquals(form.getMovieName(), updatedMovie.getMovieName());
  }

  @Test
  void update_WithConflictVersion_ShouldHandleOptimisticLockingFailure() throws Exception {

    // Given: データベースから現在のデータを取得
    MovieEntity movie = movieRepository.findAll().getFirst();
    Integer currentId = movie.getMovieId();
    Integer currentVersion = movie.getVersion(); // 現在のバージョンを取得

    // 別スレッドや別の処理で既にバージョンが更新されたと仮定し、リクエストを送る直前にDB側のバージョンだけを上げておく
    movie.setMovieName("Concurrent Update");
    movieRepository.saveAndFlush(movie); // これでDB上のバージョンが上がる
    entityManager.clear();

    MovieForm form = new MovieForm();
    form.setMovieId(currentId);
    form.setMovieName("Try to Update");
    form.setVersion(currentVersion);

    // When & Then
    performUpdateRequest(form).andExpect(status().isOk()).andExpect(view().name("error"))
        .andExpect(model().attribute("isOptimisticLockError", true));
  }

  @Test
  void delete_ExistingMovie_ShouldDeleteAndRedirectToList() throws Exception {

    // Given
    MovieEntity targetMovie = movieRepository.findAll().getFirst();
    Integer targetMovieId = targetMovie.getMovieId();

    // When & Then
    performDeleteRequest(targetMovie.getMovieId()).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(LIST_URL));

    entityManager.flush();
    entityManager.clear();

    MovieEntity movie = movieRepository.findById(targetMovieId).orElse(null);
    Assertions.assertNull(movie);
  }

  // --- Helper Methods ---

  private ResultActions performGetListRequest() throws Exception {
    return mockMvc.perform(get(LIST_ENDPOINT)).andDo(print());
  }

  private ResultActions performGetDetailRequest(int movieId) throws Exception {
    return mockMvc
        .perform(
            get(DETAIL_ENDPOINT, movieId).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performGetRegisterRequest() throws Exception {
    return mockMvc
        .perform(get(REGISTER_ENDPOINT).accept(MediaType.TEXT_HTML).characterEncoding("UTF-8"))
        .andDo(print());
  }

  private ResultActions performRegisterRequest(String movieName) throws Exception {
    return mockMvc
        .perform(post(REGISTER_ENDPOINT).with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED).param("movieName", movieName))
        .andDo(print());
  }

  private ResultActions performUpdateRequest(MovieForm form) throws Exception {
    return mockMvc.perform(post(UPDATE_ENDPOINT).with(csrf()).param("update", "")
        .param("movieId", form.getMovieId().toString()).param("movieName", form.getMovieName())
        .param("version", form.getVersion().toString())).andDo(print());
  }

  private ResultActions performDeleteRequest(int movieId) throws Exception {
    return mockMvc.perform(post(DELETE_ENDPOINT).with(csrf()).param("delete", "").param("movieId",
        String.valueOf(movieId))).andDo(print());
  }
}
