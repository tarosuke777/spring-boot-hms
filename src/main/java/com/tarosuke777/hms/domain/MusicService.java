package com.tarosuke777.hms.domain;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.entity.MusicEntity;
import com.tarosuke777.hms.form.MusicForm;
import com.tarosuke777.hms.repository.MusicRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicService {

  private final MusicRepository musicRepository;
  private final EntityManager entityManager;
  private final ModelMapper modelMapper;

  public List<MusicForm> getMusicList() {
    return musicRepository.findAll().stream()
        .map(entity -> modelMapper.map(entity, MusicForm.class)).toList();
  }

  public MusicForm getMusicDetails(Integer musicId) {
    MusicEntity music = musicRepository.findById(musicId)
        .orElseThrow(() -> new RuntimeException("Music not found"));
    MusicForm musicForm = modelMapper.map(music, MusicForm.class);
    if (music.getArtist() != null) {
      musicForm.setArtistId(music.getArtist().getArtistId());
    }
    return musicForm;
  }

  @Transactional
  public void registerMusic(MusicForm form) {

    MusicEntity entity = modelMapper.map(form, MusicEntity.class);

    // artistId を ArtistEntity の参照としてセット
    if (form.getArtistId() != null) {
      entity.setArtist(entityManager.getReference(ArtistEntity.class, form.getArtistId()));
    }

    musicRepository.save(entity);
  }

  @Transactional
  public void updateMusic(MusicForm form) {
    MusicEntity entity = modelMapper.map(form, MusicEntity.class);

    if (form.getArtistId() != null) {
      entity.setArtist(entityManager.getReference(ArtistEntity.class, form.getArtistId()));
    }

    musicRepository.save(entity);
  }

  @Transactional
  public void deleteMusic(Integer musicId) {
    musicRepository.deleteById(musicId);
  }

  @Tool(description = "好きな曲名の一覧を取得する")
  public String getMusicListForMcp() {
    return getMusicList().stream().map(MusicForm::getMusicName).reduce((a, b) -> a + ", " + b)
        .orElse("No music available");
  }
}
