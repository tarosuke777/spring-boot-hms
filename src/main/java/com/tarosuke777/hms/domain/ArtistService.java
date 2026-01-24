package com.tarosuke777.hms.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.form.ArtistForm;
import com.tarosuke777.hms.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistService {

  private final ArtistRepository artistRepository;
  private final ModelMapper modelMapper;

  public List<ArtistForm> getArtistList() {
    return artistRepository.findAll().stream()
        .map(entity -> modelMapper.map(entity, ArtistForm.class)).toList();
  }

  public ArtistForm getArtist(Integer artistId) {
    ArtistEntity artist = artistRepository.findById(artistId)
        .orElseThrow(() -> new RuntimeException("Artist not found"));
    return modelMapper.map(artist, ArtistForm.class);
  }

  @Transactional
  public void registerArtist(ArtistForm form) {
    ArtistEntity entity = modelMapper.map(form, ArtistEntity.class);
    artistRepository.save(entity);
  }

  @Transactional
  public void updateArtist(ArtistForm form) {
    ArtistEntity existEntity = artistRepository.findById(form.getArtistId())
        .orElseThrow(() -> new RuntimeException("Artist not found"));
    ArtistEntity entity = new ArtistEntity();
    modelMapper.map(existEntity, entity);
    modelMapper.map(form, entity);
    artistRepository.save(entity);
  }

  @Transactional
  public void deleteArtist(Integer artistId) {
    artistRepository.deleteById(artistId);
  }

  public Map<Integer, String> getArtistMap() {
    return artistRepository.findAll().stream().collect(Collectors.toMap(ArtistEntity::getArtistId,
        ArtistEntity::getArtistName, (existing, replacement) -> existing, LinkedHashMap::new));
  }
}
