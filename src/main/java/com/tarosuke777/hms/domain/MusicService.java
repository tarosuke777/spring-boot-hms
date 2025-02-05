package com.tarosuke777.hms.domain;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.entity.MusicEntity;
import com.tarosuke777.hms.form.MusicForm;
import com.tarosuke777.hms.repository.MusicMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MusicService {
  private final MusicMapper musicMapper;
  private final ModelMapper modelMapper;

  public List<MusicForm> getMusicList() {
    return musicMapper.findMany().stream()
        .map(entity -> modelMapper.map(entity, MusicForm.class))
        .toList();
  }

  public MusicForm getMusicDetails(Integer musicId) {
    MusicEntity music = musicMapper.findOne(musicId);
    MusicForm musicForm = modelMapper.map(music, MusicForm.class);
    musicForm.setArtistId(music.getArtist().getArtistId());
    return musicForm;
  }

  @Transactional
  public void registerMusic(MusicForm form) {
    musicMapper.insertOne(form.getMusicName(), form.getArtistId(), form.getLink());
  }

  @Transactional
  public void updateMusic(MusicForm form) {
    musicMapper.updateOne(form.getMusicId(), form.getMusicName(), form.getArtistId());
  }

  @Transactional
  public void deleteMusic(Integer musicId) {
    musicMapper.deleteOne(musicId);
  }
}
