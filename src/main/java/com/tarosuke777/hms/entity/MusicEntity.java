package com.tarosuke777.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class MusicEntity {

  private Integer musicId;
  private String musicName;
  private ArtistEntity artist;
  private String link;

}
