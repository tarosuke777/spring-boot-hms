package com.tarosuke777.hms.domain;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.form.BookForm;
import com.tarosuke777.hms.mapper.BookMapper;
import com.tarosuke777.hms.repository.BookRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;
  private final EntityManager entityManager;
  private final BookMapper bookMapper;

  public List<BookForm> getBookList(String currentUserId) {
    return bookRepository.findByCreatedByOrderByIdAsc(currentUserId).stream().map(book -> {
      BookForm form = bookMapper.toForm(book);
      if (book.getAuthor() != null) {
        form.setAuthorId(book.getAuthor().getId());
      }
      return form;
    }).toList();
  }

  public BookForm getBookDetails(Integer id, String currentUserId) {
    BookEntity book = bookRepository.findByIdAndCreatedBy(id, currentUserId)
        .orElseThrow(() -> new RuntimeException("Book not found"));
    BookForm bookForm = bookMapper.toForm(book);
    bookForm.setAuthorId(book.getAuthor().getId());
    return bookForm;
  }

  @Transactional
  public void registerBook(BookForm form) {
    BookEntity entity = bookMapper.toEntity(form);
    if (form.getAuthorId() != null) {
      entity.setAuthor(entityManager.getReference(AuthorEntity.class, form.getAuthorId()));
    }
    bookRepository.save(entity);
  }

  @Transactional
  public void updateBook(BookForm form, String currentUserId) {
    BookEntity existEntity = bookRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
        .orElseThrow(() -> new RuntimeException("Book not found"));

    BookEntity entity = bookMapper.copy(existEntity);
    if (existEntity.getAuthor() != null) {
      entity.setAuthor(
          entityManager.getReference(AuthorEntity.class, existEntity.getAuthor().getId()));
    }

    bookMapper.updateEntityFromForm(form, entity);
    if (form.getAuthorId() != null) {
      entity.setAuthor(entityManager.getReference(AuthorEntity.class, form.getAuthorId()));
    }
    bookRepository.save(entity);
  }

  @Transactional
  public void deleteBook(Integer id, String currentUserId) {
    if (!bookRepository.existsByIdAndCreatedBy(id, currentUserId)) {
      throw new RuntimeException("Book not found or access denied");
    }
    bookRepository.deleteById(id);
  }
}
