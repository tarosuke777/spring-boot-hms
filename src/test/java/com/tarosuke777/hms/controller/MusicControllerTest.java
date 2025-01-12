package com.tarosuke777.hms.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.entity.ArtistEntity;
import com.tarosuke777.hms.entity.MusicEntity;
import com.tarosuke777.hms.form.MusicForm;
import com.tarosuke777.hms.repository.MusicMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@WithUserDetails("admin@tarosuke777.com")
public class MusicControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private MusicMapper musicMapper;

  @Test
  void getListTest() throws Exception {

    List<MusicForm> expMusicList = new ArrayList<>();
    expMusicList.add(new MusicForm(1, "好きになってはいけない理由", 1));
    expMusicList.add(new MusicForm(2, "ゆずれない", 1));
    expMusicList.add(new MusicForm(3, "サクラメイキュウ", 2));

    Map<Integer, String> expArtistMap = new HashMap<>();
    expArtistMap.put(1, "藤川千愛");
    expArtistMap.put(2, "分島花音");

    this.mockMvc
        .perform(get("/music/list"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expArtistMap))
        .andExpect(model().attribute("musicList", expMusicList))
        .andExpect(view().name("music/list"))
        .andReturn();
  }

  @Test
  void getDetailTest() throws Exception {

    MusicForm musicForm = new MusicForm(1, "好きになってはいけない理由", 1);

    Map<Integer, String> expArtistMap = new HashMap<>();
    expArtistMap.put(1, "藤川千愛");
    expArtistMap.put(2, "分島花音");

    this.mockMvc
        .perform(get("/music/detail/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expArtistMap))
        .andExpect(model().attribute("musicForm", musicForm))
        .andExpect(view().name("music/detail"))
        .andReturn();
  }

  @Test
  void getRegisterTest() throws Exception {

    MusicForm musicForm = new MusicForm();

    Map<Integer, String> expArtistMap = new HashMap<>();
    expArtistMap.put(1, "藤川千愛");
    expArtistMap.put(2, "分島花音");

    this.mockMvc
        .perform(get("/music/register"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expArtistMap))
        .andExpect(model().attribute("musicForm", musicForm))
        .andExpect(view().name("music/register"))
        .andReturn();
  }

  @Test
  void registerTest() throws Exception {

    List<MusicEntity> expMusicList = new ArrayList<>();
    expMusicList.add(new MusicEntity(1, "好きになってはいけない理由", new ArtistEntity(1, "藤川千愛")));
    expMusicList.add(new MusicEntity(2, "ゆずれない", new ArtistEntity(1, "藤川千愛")));
    expMusicList.add(new MusicEntity(3, "サクラメイキュウ", new ArtistEntity(2, "分島花音")));
    expMusicList.add(new MusicEntity(4, "test", new ArtistEntity(1, "藤川千愛")));

    this.mockMvc
        .perform(
            post("/music/register").with(csrf()).param("musicName", "test").param("artistId", "1"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/music/list"))
        .andReturn();

    List<MusicEntity> musicList = musicMapper.findMany();
    assertIterableEquals(expMusicList, musicList);
  }

  @Test
  void updateTest() throws Exception {

    MusicEntity expMusic = musicMapper.findOne(1);
    expMusic.setMusicName("好きになってはいけない理由2");

    this.mockMvc
        .perform(
            post("/music/detail")
                .with(csrf())
                .param("update", "")
                .param("musicId", expMusic.getMusicId().toString())
                .param("musicName", expMusic.getMusicName())
                .param("artistId", expMusic.getArtist().getArtistId().toString()))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/music/list"))
        .andReturn();

    MusicEntity music = musicMapper.findOne(1);

    assertEquals(music, expMusic);
  }

  @Test
  void deleteTest() throws Exception {

    MusicEntity targetMusic = musicMapper.findOne(1);

    this.mockMvc
        .perform(
            post("/music/detail")
                .with(csrf())
                .param("delete", "")
                .param("musicId", targetMusic.getMusicId().toString()))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/music/list"))
        .andReturn();

    MusicEntity music = musicMapper.findOne(1);

    assertNull(music);
  }
}
