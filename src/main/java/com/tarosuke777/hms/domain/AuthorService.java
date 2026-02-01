package com.tarosuke777.hms.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.form.AuthorForm;
import com.tarosuke777.hms.mapper.AuthorMapper;
import com.tarosuke777.hms.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;
  private final AuthorMapper authorMapper;

  public List<AuthorForm> getAuthorList(String currentUserId) {
    return authorRepository.findByCreatedBy(currentUserId).stream().map(authorMapper::toForm)
        .toList();
  }

  public AuthorForm getAuthor(Integer authorId, String currentUserId) {
    AuthorEntity author = authorRepository.findByAuthorIdAndCreatedBy(authorId, currentUserId)
        .orElseThrow(() -> new RuntimeException("Author not found or access denied"));
    return authorMapper.toForm(author);
  }

  @Transactional
  public void registerAuthor(AuthorForm form) {
    AuthorEntity entity = authorMapper.toEntity(form);
    authorRepository.save(entity);
  }

  @Transactional
  public void updateAuthor(AuthorForm form, String currentUserId) {
    AuthorEntity existEntity =
        authorRepository.findByAuthorIdAndCreatedBy(form.getAuthorId(), currentUserId)
            .orElseThrow(() -> new RuntimeException("Author not found or access denied"));
    AuthorEntity entity = authorMapper.copy(existEntity);
    authorMapper.updateEntityFromForm(form, entity);
    authorRepository.save(entity);
  }

  @Transactional
  public void deleteAuthor(Integer authorId, String currentUserId) {
    if (!authorRepository.existsByAuthorIdAndCreatedBy(authorId, currentUserId)) {
      throw new RuntimeException("Author not found or access denied");
    }
    authorRepository.deleteById(authorId);
  }

  public Map<Integer, String> getAuthorMap() {
    return authorRepository.findAll().stream().collect(Collectors.toMap(AuthorEntity::getAuthorId,
        AuthorEntity::getAuthorName, (existing, replacement) -> existing, LinkedHashMap::new));
  }
}
