package com.tarosuke777.hms.domain;

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

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

    UserEntity loginUser = userRepository.findByUserName(userName)
        .orElseThrow(() -> new UsernameNotFoundException("user not found."));

    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(loginUser.getRole()));

    return new User(loginUser.getUserName(), loginUser.getPassword(), authorities);
  }
}
