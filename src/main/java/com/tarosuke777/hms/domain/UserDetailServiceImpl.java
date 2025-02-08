package com.tarosuke777.hms.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.repository.UserMapper;

public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired private UserMapper mapper;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

    UserEntity loginUser = mapper.findLoginUser(userName);

    if (loginUser == null) {
      throw new UsernameNotFoundException("user not found.");
    }

    GrantedAuthority authority = new SimpleGrantedAuthority(loginUser.getRole());
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(authority);

    UserDetails userDetails =
        (UserDetails) new User(loginUser.getUserName(), loginUser.getPassword(), authorities);

    return userDetails;
  }
}
