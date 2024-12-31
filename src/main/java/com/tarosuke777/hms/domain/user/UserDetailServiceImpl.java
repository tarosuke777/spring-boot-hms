package com.tarosuke777.hms.domain.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tarosuke777.hms.repository.UserMapper;
import com.tarosuke777.hms.repository.entity.UserEntity;

public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired private UserMapper mapper;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    UserEntity loginUser = mapper.findLoginUser(email);

    if (loginUser == null) {
      throw new UsernameNotFoundException("user not found.");
    }

    GrantedAuthority authority = new SimpleGrantedAuthority(loginUser.getRole());
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(authority);

    UserDetails userDetails =
        (UserDetails) new User(loginUser.getEmail(), loginUser.getPassword(), authorities);

    return userDetails;
  }
}
