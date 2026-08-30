package arpa.home.hms.controller.api;

import arpa.home.hms.form.MovieForm;
import arpa.home.hms.security.LoginUser;
import arpa.home.hms.service.MovieService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieApiController {

  private final MovieService movieService;

  @GetMapping
  public List<MovieForm> getMovies(@AuthenticationPrincipal LoginUser user) {
    return movieService.getMovieList(user.getId());
  }
}
