package arpa.home.hms.service;

import arpa.home.hms.entity.GameVideoEntity;
import arpa.home.hms.form.GameVideoForm;
import arpa.home.hms.repository.GameVideoRepository;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GameVideoService {

  private final GameVideoRepository gameVideoRepository;
  private final String videosBaseUrl;

  public GameVideoService(GameVideoRepository gameVideoRepository,
      @Value("${app.services.videos-url}") String videosBaseUrl) {
    this.gameVideoRepository = gameVideoRepository;
    this.videosBaseUrl = videosBaseUrl;
  }

  public Page<GameVideoForm> getGameVideoPage(Pageable pageable) {
    return gameVideoRepository.findAllByOrderByCreatedAtDescIdDesc(Objects.requireNonNull(pageable))
        .map(this::toForm);
  }

  public GameVideoForm getGameVideo(Long id) {
    GameVideoEntity video = gameVideoRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game video not found"));
    return toForm(video);
  }

  private GameVideoForm toForm(GameVideoEntity video) {
    String videoUrl = UriComponentsBuilder.fromUriString(videosBaseUrl)
        .pathSegment(video.getFileName()).encode(StandardCharsets.UTF_8).toUriString();
    return new GameVideoForm(video.getId(), video.getTitle(), video.getName(), video.getFileName(),
        video.getCreatedAt(), video.getUpdatedAt(), videoUrl);
  }
}
