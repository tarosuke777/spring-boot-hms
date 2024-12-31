package com.tarosuke777.hms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.controller.form.MusicForm;

/**
 * https://spring.pleiades.io/spring-security/reference/servlet/test/mockmvc/setup.html
 * https://spring.pleiades.io/spring-security/reference/servlet/test/mockmvc/authentication.html
 * https://github.com/OpenRefine/OpenRefine/issues/6913
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql("classpath:schema/test.sql")
public class MusicControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @WithUserDetails("admin@tarosuke777.com")
  void shouldReturnDefaultMessage() throws Exception {

    List<MusicForm> expMusicList = new ArrayList<>();

    MusicForm musicForm1 = new MusicForm();
    musicForm1.setMusicId(1);
    musicForm1.setMusicName("好きになってはいけない理由");
    musicForm1.setArtistId(1);
    MusicForm musicForm2 = new MusicForm();
    musicForm2.setMusicId(2);
    musicForm2.setMusicName("ゆずれない");
    musicForm2.setArtistId(1);
    MusicForm musicForm3 = new MusicForm();
    musicForm3.setMusicId(3);
    musicForm3.setMusicName("サクラメイキュウ");
    musicForm3.setArtistId(2);
    expMusicList.add(musicForm1);
    expMusicList.add(musicForm2);
    expMusicList.add(musicForm3);

    Map<Integer, String> expArtistMap = new HashMap<>();
    expArtistMap.put(1, "藤川千愛");
    expArtistMap.put(2, "分島花音");
    expArtistMap.put(3, "test");

    this.mockMvc
        .perform(get("/music/list"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attribute("artistMap", expArtistMap))
        .andExpect(model().attribute("musicList", expMusicList))
        .andExpect(view().name("music/list"))
        .andReturn();
  }
}
