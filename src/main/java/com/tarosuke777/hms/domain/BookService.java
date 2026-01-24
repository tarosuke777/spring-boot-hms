package com.tarosuke777.hms.domain;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.AuthorEntity;
import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.form.BookForm;
import com.tarosuke777.hms.repository.BookRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;
  private final EntityManager entityManager;
  private final ModelMapper modelMapper;

  public List<BookForm> getBookList() {
    return bookRepository.findAllByOrderByBookIdAsc().stream()
        .map(entity -> modelMapper.map(entity, BookForm.class)).toList();
  }

  public BookForm getBookDetails(Integer bookId) {
    BookEntity book =
        bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
    BookForm bookForm = modelMapper.map(book, BookForm.class);
    bookForm.setAuthorId(book.getAuthor().getAuthorId());
    return bookForm;
  }

  @Transactional
  public void registerBook(BookForm form) {
    BookEntity entity = modelMapper.map(form, BookEntity.class);
    if (form.getAuthorId() != null) {
      entity.setAuthor(entityManager.getReference(AuthorEntity.class, form.getAuthorId()));
    }
    bookRepository.save(entity);
  }

  @Transactional
  public void updateBook(BookForm form) {
    BookEntity existEntity = bookRepository.findById(form.getBookId())
        .orElseThrow(() -> new RuntimeException("Book not found"));

    BookEntity entity = new BookEntity();
    modelMapper.map(existEntity, entity);
    if (existEntity.getAuthor() != null) {
      entity.setAuthor(
          entityManager.getReference(AuthorEntity.class, existEntity.getAuthor().getAuthorId()));
    }

    modelMapper.map(form, entity);
    if (form.getAuthorId() != null) {
      entity.setAuthor(entityManager.getReference(AuthorEntity.class, form.getAuthorId()));
    }
    bookRepository.save(entity);
  }

  @Transactional
  public void deleteBook(Integer bookId) {
    bookRepository.deleteById(bookId);
  }
}
