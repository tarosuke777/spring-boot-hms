package com.tarosuke777.hms.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.CastEntity;
import com.tarosuke777.hms.entity.MovieEntity;
import com.tarosuke777.hms.form.MovieForm;
import com.tarosuke777.hms.mapper.MovieMapper;
import com.tarosuke777.hms.repository.MovieRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

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
        MovieEntity entity = movieMapper.toEntity(form);

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
        MovieEntity entity = movieMapper.copy(existEntity);

        if (existEntity.getCast() != null) {
            entity.setCast(
                    entityManager.getReference(CastEntity.class, existEntity.getCast().getId()));
        }

        movieMapper.updateEntityFromForm(form, entity);
        if (form.getCastId() != null) {
            entity.setCast(entityManager.getReference(CastEntity.class, form.getCastId()));
        }

        movieRepository.save(entity);
    }

    /** 削除 */
    @Transactional
    public void deleteMovie(Integer id, Integer currentUserId) {
        if (!movieRepository.existsByIdAndCreatedBy(id, currentUserId)) {
            throw new RuntimeException("Movie not found or access denied");
        }
        movieRepository.deleteById(id);
    }
}
