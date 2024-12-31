package com.tarosuke777.hms.repository.entity;

import lombok.Data;

@Data
public class MusicEntity {

  private Integer musicId;
  private String musicName;
  private ArtistEntity artist;
}
