package com.tarosuke777.hms.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.form.AuthorForm;
import com.tarosuke777.hms.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorService {

  private final AuthorRepository authorRepository;
  private final ModelMapper modelMapper;

  public List<AuthorForm> getAuthorList(String currentUserId) {
    return authorRepository.findByCreatedBy(currentUserId).stream()
        .map(entity -> modelMapper.map(entity, AuthorForm.class)).toList();
  }

  public AuthorForm getAuthor(Integer authorId, String currentUserId) {
    AuthorEntity author = authorRepository.findByAuthorIdAndCreatedBy(authorId, currentUserId)
        .orElseThrow(() -> new RuntimeException("Author not found or access denied"));
    return modelMapper.map(author, AuthorForm.class);
  }

  @Transactional
  public void registerAuthor(AuthorForm form) {
    AuthorEntity entity = modelMapper.map(form, AuthorEntity.class);
    authorRepository.save(entity);
  }

  @Transactional
  public void updateAuthor(AuthorForm form, String currentUserId) {
    AuthorEntity existEntity =
        authorRepository.findByAuthorIdAndCreatedBy(form.getAuthorId(), currentUserId)
            .orElseThrow(() -> new RuntimeException("Author not found or access denied"));
    AuthorEntity entity = new AuthorEntity();
    modelMapper.map(existEntity, entity);
    modelMapper.map(form, entity);
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
