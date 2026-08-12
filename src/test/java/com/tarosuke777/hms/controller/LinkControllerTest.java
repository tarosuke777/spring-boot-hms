package com.tarosuke777.hms.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Sql
@WithUserDetails("admin")
public class LinkControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getList_ShouldReturnLinkList() throws Exception {
    mockMvc.perform(get("/link/list")).andDo(print()).andExpect(status().isOk())
        .andExpect(view().name("link/list"))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("AdGuard Home")))
        .andExpect(content()
            .string(org.hamcrest.Matchers.containsString("https://adg.home.arpa/login.html")));
  }
}
