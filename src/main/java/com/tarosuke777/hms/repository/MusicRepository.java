package com.tarosuke777.hms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import com.tarosuke777.hms.entity.MusicEntity;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, Integer> {

    /**
     * findMany に相当<br>
     * ArtistEntity を Fetch Join して効率的に全件取得する
     */
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"artist"})
    List<MusicEntity> findAll();
}
