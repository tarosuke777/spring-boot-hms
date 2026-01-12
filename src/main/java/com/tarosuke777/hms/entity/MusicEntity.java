package com.tarosuke777.hms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "music")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer musicId;

  private String musicName;

  @ManyToOne(fetch = FetchType.LAZY)
  private ArtistEntity artist;
  private String link;

}
