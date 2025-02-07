package com.tarosuke777.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class AuthorEntity {

  private Integer authorId;
  private String authorName;
}
