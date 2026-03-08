package com.tarosuke777.hms.service;

import java.util.List;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.entity.MusicEntity;
import com.tarosuke777.hms.form.MusicForm;
import com.tarosuke777.hms.mapper.MusicMapper;
import com.tarosuke777.hms.repository.MusicRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicService {

  private final MusicRepository musicRepository;
  private final EntityManager entityManager;
  private final MusicMapper musicMapper;

  public List<MusicForm> getMusicList(String currentUserId) {
    return musicRepository.findByCreatedByOrderByIdAsc(currentUserId).stream().map(music -> {
      MusicForm form = musicMapper.toForm(music);
      if (music.getArtist() != null) {
        form.setArtistId(music.getArtist().getId());
      }
      return form;
    }).toList();
  }

  public MusicForm getMusicDetails(Integer id, String currentUserId) {
    MusicEntity music = musicRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Music not found"));
    MusicForm musicForm = musicMapper.toForm(music);
    if (music.getArtist() != null) {
      musicForm.setArtistId(music.getArtist().getId());
    }
    return musicForm;
  }

  @Transactional
  public void registerMusic(MusicForm form) {

    MusicEntity entity = musicMapper.toEntity(form);

    // artistId を ArtistEntity の参照としてセット
    if (form.getArtistId() != null) {
      entity.setArtist(entityManager.getReference(ArtistEntity.class, form.getArtistId()));
    }

    musicRepository.save(entity);
  }

  @Transactional
  public void updateMusic(MusicForm form, String currentUserId) {
    MusicEntity existEntity = musicRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
        .orElseThrow(() -> new RuntimeException("Music not found"));
    MusicEntity entity = musicMapper.copy(existEntity);
    if (existEntity.getArtist() != null) {
      entity.setArtist(
          entityManager.getReference(ArtistEntity.class, existEntity.getArtist().getId()));
    }

    musicMapper.updateEntityFromForm(form, entity);
    if (form.getArtistId() != null) {
      entity.setArtist(entityManager.getReference(ArtistEntity.class, form.getArtistId()));
    }

    musicRepository.save(entity);
  }

  @Transactional
  public void deleteMusic(Integer id, String currentUserId) {
    if (!musicRepository.existsByIdAndCreatedBy(id, currentUserId)) {
      throw new RuntimeException("Music not found");
    }
    musicRepository.deleteById(id);
  }

  @Tool(description = "好きな曲名の一覧を取得する")
  public String getMusicListForMcp(String currentUserId) {
    return musicRepository.findAll().stream().map(MusicEntity::getName)
        .reduce((a, b) -> a + ", " + b).orElse("No music available");
  }
}
