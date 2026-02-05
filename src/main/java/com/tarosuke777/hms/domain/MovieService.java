package com.tarosuke777.hms.domain;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.MovieEntity;
import com.tarosuke777.hms.form.MovieForm;
import com.tarosuke777.hms.mapper.MovieMapper;
import com.tarosuke777.hms.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    /** 一覧取得 */
    public List<MovieForm> getMovieList(String currentUserId) {
        return movieRepository.findByCreatedBy(currentUserId).stream()
                .map(entity -> movieMapper.toForm(entity)).toList();
    }

    /** 1件取得 */
    public MovieForm getMovie(Integer movieId, String currentUserId) {
        MovieEntity movie = movieRepository.findByMovieIdAndCreatedBy(movieId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return movieMapper.toForm(movie);
    }

    /** 登録 */
    @Transactional
    public void registerMovie(MovieForm form) {
        MovieEntity entity = movieMapper.toEntity(form);
        movieRepository.save(entity);
    }

    /** 更新 */
    @Transactional
    public void updateMovie(MovieForm form, String currentUserId) {
        MovieEntity existEntity = movieRepository
                .findByMovieIdAndCreatedBy(form.getMovieId(), currentUserId)
                .orElseThrow(() -> new RuntimeException("Movie not found or access denied"));
        MovieEntity entity = movieMapper.copy(existEntity);
        movieMapper.updateEntityFromForm(form, entity);
        movieRepository.save(entity);
    }

    /** 削除 */
    @Transactional
    public void deleteMovie(Integer movieId, String currentUserId) {
        if (!movieRepository.existsByMovieIdAndCreatedBy(movieId, currentUserId)) {
            throw new RuntimeException("Movie not found or access denied");
        }
        movieRepository.deleteById(movieId);
    }
}
