package com.tarosuke777.hms.service;

import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.form.ArtistForm;
import com.tarosuke777.hms.mapper.ArtistMapper;
import com.tarosuke777.hms.repository.ArtistRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService {

  private final ArtistRepository artistRepository;
  private final ArtistMapper artistMapper;

  public List<ArtistForm> getArtistList(Integer currentUserId) {
    return artistRepository.findByCreatedBy(currentUserId).stream().map(artistMapper::toForm)
        .toList();
  }

  public ArtistForm getArtist(Integer artistId, Integer currentUserId) {
    ArtistEntity artist = artistRepository.findByIdAndCreatedBy(artistId, currentUserId)
        .orElseThrow(() -> new RuntimeException("Artist not found or access denied"));
    return artistMapper.toForm(artist);
  }

  @Transactional
  public void registerArtist(ArtistForm form) {
    ArtistEntity entity = Objects.requireNonNull(artistMapper.toEntity(form));
    artistRepository.save(entity);
  }

  @Transactional
  public void updateArtist(ArtistForm form, Integer currentUserId) {
    ArtistEntity existEntity = artistRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
        .orElseThrow(() -> new RuntimeException("Artist not found or access denied"));
    ArtistEntity entity = Objects.requireNonNull(artistMapper.copy(existEntity));
    artistMapper.updateEntityFromForm(form, entity);
    artistRepository.save(entity);
  }

  @Transactional
  public void deleteArtist(@NonNull Integer artistId, Integer currentUserId) {
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
