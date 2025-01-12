package com.tarosuke777.hms.entity;

import lombok.Data;

@Data
public class UserEntity {

  private Integer userId;
  private String userName;
  private String password;
  private String role;
  private String email;
}
