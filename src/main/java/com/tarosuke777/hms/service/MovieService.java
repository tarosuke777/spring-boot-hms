package com.tarosuke777.hms.service;

import com.tarosuke777.hms.entity.CastEntity;
import com.tarosuke777.hms.entity.MovieEntity;
import com.tarosuke777.hms.form.MovieForm;
import com.tarosuke777.hms.mapper.MovieMapper;
import com.tarosuke777.hms.repository.MovieRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;
  private final EntityManager entityManager;
  private final MovieMapper movieMapper;

  /** 一覧取得 */
  public List<MovieForm> getMovieList(Integer currentUserId) {
    return movieRepository.findByCreatedBy(currentUserId).stream().map(movie -> {
      MovieForm form = movieMapper.toForm(movie);
      if (movie.getCast() != null) {
        form.setCastId(movie.getCast().getId());
      }
      return form;
    }).toList();
  }

  /** ページング一覧取得 */
  public Page<MovieForm> getMoviePage(Integer currentUserId, @NonNull Pageable pageable) {
    return movieRepository.findByCreatedBy(currentUserId, pageable).map(movie -> {
      MovieForm form = movieMapper.toForm(movie);
      if (movie.getCast() != null) {
        form.setCastId(movie.getCast().getId());
      }
      return form;
    });
  }

  /** 1件取得 */
  public MovieForm getMovie(Integer id, Integer currentUserId) {
    MovieEntity movie = movieRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Movie not found"));

    MovieForm form = movieMapper.toForm(movie);
    if (movie.getCast() != null) {
      form.setCastId(movie.getCast().getId());
    }

    return form;
  }

  /** 登録 */
  @Transactional
  public void registerMovie(MovieForm form) {
    MovieEntity entity = Objects.requireNonNull(movieMapper.toEntity(form));

    if (form.getCastId() != null) {
      entity.setCast(entityManager.getReference(CastEntity.class, form.getCastId()));
    }

    movieRepository.save(entity);
  }

  /** 更新 */
  @Transactional
  public void updateMovie(MovieForm form, Integer currentUserId) {
    MovieEntity existEntity = movieRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
        .orElseThrow(() -> new RuntimeException("Movie not found or access denied"));
    MovieEntity entity = Objects.requireNonNull(movieMapper.copy(existEntity));

    if (existEntity.getCast() != null) {
      entity.setCast(entityManager.getReference(CastEntity.class, existEntity.getCast().getId()));
    }

    movieMapper.updateEntityFromForm(form, entity);
    if (form.getCastId() != null) {
      entity.setCast(entityManager.getReference(CastEntity.class, form.getCastId()));
    }

    movieRepository.save(entity);
  }

  /** 削除 */
  @Transactional
  public void deleteMovie(@NonNull Integer id, Integer currentUserId) {
    if (!movieRepository.existsByIdAndCreatedBy(id, currentUserId)) {
      throw new RuntimeException("Movie not found or access denied");
    }
    movieRepository.deleteById(id);
  }

  /** Cast に紐づく Movie 一覧取得 */
  public List<MovieForm> getMoviesByCast(Integer castId, Integer currentUserId) {
    return movieRepository.findByCastIdAndCreatedBy(castId, currentUserId).stream().map(movie -> {
      MovieForm form = movieMapper.toForm(movie);
      form.setCastId(castId);
      return form;
    }).toList();
  }
}
