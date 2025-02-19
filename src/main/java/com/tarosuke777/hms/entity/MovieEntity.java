package com.tarosuke777.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class MovieEntity {

  private Integer movieId;
  private String movieName;
  private String note;
}
