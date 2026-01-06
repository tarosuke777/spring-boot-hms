package com.tarosuke777.hms.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.repository.AuthorMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {
  private final AuthorMapper authorMapper;

  public Map<Integer, String> getAuthorMap() {
    return authorMapper.findMany().stream().collect(Collectors.toMap(AuthorEntity::getAuthorId,
        AuthorEntity::getAuthorName, (existing, replacement) -> existing, LinkedHashMap::new));
  }
}
