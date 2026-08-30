package arpa.home.hms.service;

import arpa.home.hms.entity.UserEntity;
import arpa.home.hms.enums.Role;
import arpa.home.hms.repository.UserRepository;
import arpa.home.hms.security.LoginUser;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    return new LoginUser(loginUser.getId(), loginUser.getName(), loginUser.getPassword(),
        authorities);
  }
}
