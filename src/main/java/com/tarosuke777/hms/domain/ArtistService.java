package com.tarosuke777.hms.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.repository.ArtistMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistService {
  private final ArtistMapper artistMapper;

  @Cacheable("artistMap")
  public Map<Integer, String> getArtistMap() {
    return artistMapper.findMany().stream().collect(Collectors.toMap(ArtistEntity::getArtistId,
        ArtistEntity::getArtistName, (existing, replacement) -> existing, LinkedHashMap::new));
  }
}
