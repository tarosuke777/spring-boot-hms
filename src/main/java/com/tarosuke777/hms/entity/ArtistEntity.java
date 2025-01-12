package com.tarosuke777.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class ArtistEntity {

  private Integer artistId;
  private String artistName;
}
