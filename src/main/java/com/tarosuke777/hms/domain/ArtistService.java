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

  public List<ArtistForm> getArtistList(String currentUserId) {
    return artistRepository.findByCreatedBy(currentUserId).stream()
        .map(entity -> modelMapper.map(entity, ArtistForm.class)).toList();
  }

  public ArtistForm getArtist(Integer artistId, String currentUserId) {
    ArtistEntity artist = artistRepository.findByIdAndCreatedBy(artistId, currentUserId)
        .orElseThrow(() -> new RuntimeException("Artist not found or access denied"));
    return modelMapper.map(artist, ArtistForm.class);
  }

  @Transactional
  public void registerArtist(ArtistForm form) {
    ArtistEntity entity = modelMapper.map(form, ArtistEntity.class);
    artistRepository.save(entity);
  }

  @Transactional
  public void updateArtist(ArtistForm form, String currentUserId) {
    ArtistEntity existEntity = artistRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
        .orElseThrow(() -> new RuntimeException("Artist not found or access denied"));
    ArtistEntity entity = new ArtistEntity();
    modelMapper.map(existEntity, entity);
    modelMapper.map(form, entity);
    artistRepository.save(entity);
  }

  @Transactional
  public void deleteArtist(Integer artistId, String currentUserId) {
    if (!artistRepository.existsByIdAndCreatedBy(artistId, currentUserId)) {
      throw new RuntimeException("Artist not found or access denied");
    }
    artistRepository.deleteById(artistId);
  }

  public Map<Integer, String> getArtistMap() {
    return artistRepository.findAll().stream().collect(Collectors.toMap(ArtistEntity::getId,
        ArtistEntity::getName, (existing, replacement) -> existing, LinkedHashMap::new));
  }
}
