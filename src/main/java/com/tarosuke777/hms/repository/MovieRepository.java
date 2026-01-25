package com.tarosuke777.hms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.MovieEntity;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {

    Optional<MovieEntity> findByMovieIdAndCreatedBy(Integer movieId, String createdBy);

    List<MovieEntity> findByCreatedBy(String createdBy);

    boolean existsByMovieIdAndCreatedBy(Integer movieId, String createdBy);
}
