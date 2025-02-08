package com.tarosuke777.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class BookEntity {

  private Integer bookId;
  private String bookName;
  private AuthorEntity author;
  private String link;
  private String note;
}
