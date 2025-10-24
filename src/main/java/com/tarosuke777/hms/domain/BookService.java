package com.tarosuke777.hms.domain;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.entity.BookEntity;
import com.tarosuke777.hms.form.BookForm;
import com.tarosuke777.hms.repository.BookMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookMapper bookMapper;
  private final ModelMapper modelMapper;

  public List<BookForm> getBookList() {
    return bookMapper.findMany().stream().map(entity -> modelMapper.map(entity, BookForm.class))
        .toList();
  }

  public BookForm getBookDetails(Integer bookId) {
    BookEntity book = bookMapper.findOne(bookId);
    BookForm bookForm = modelMapper.map(book, BookForm.class);
    bookForm.setAuthorId(book.getAuthor().getAuthorId());
    return bookForm;
  }

  @Transactional
  public void registerBook(BookForm form) {
    bookMapper.insertOne(form.getBookName(), form.getAuthorId(), form.getLink(), form.getNote());
  }

  @Transactional
  public void updateBook(BookForm form) {
    bookMapper.updateOne(form.getBookId(), form.getBookName(), form.getAuthorId(), form.getLink(),
        form.getNote());
  }

  @Transactional
  public void deleteBook(Integer bookId) {
    bookMapper.deleteOne(bookId);
  }
}
