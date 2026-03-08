package com.tarosuke777.hms.service;

import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tarosuke777.hms.entity.UserEntity;
import com.tarosuke777.hms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.tarosuke777.hms.enums.Role;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

    UserEntity loginUser = userRepository.findByName(userName)
        .orElseThrow(() -> new UsernameNotFoundException("user not found."));

    Role userRole = loginUser.getRole();

    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(userRole.getAuthority()));

    return new User(loginUser.getName(), loginUser.getPassword(), authorities);
  }
}
