package arpa.home.hms.service;

import arpa.home.hms.entity.GameVideoEntity;
import arpa.home.hms.form.GameVideoForm;
import arpa.home.hms.repository.GameVideoRepository;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GameVideoService {

  private final GameVideoRepository gameVideoRepository;
  private final String fileServerBaseUrl;
  private final String videosPath;

  public GameVideoService(GameVideoRepository gameVideoRepository,
      @Value("${app.file-server.base-url}") String fileServerBaseUrl,
      @Value("${app.file-server.videos-path}") String videosPath) {
    this.gameVideoRepository = gameVideoRepository;
    this.fileServerBaseUrl = fileServerBaseUrl;
    this.videosPath = videosPath;
  }

  public Page<GameVideoForm> getGameVideoPage(Pageable pageable, String title, String name) {
    return gameVideoRepository
        .findAllByTitleAndName(normalize(title), normalize(name), Objects.requireNonNull(pageable))
        .map(this::toForm);
  }

  public List<String> getGameVideoTitles() {
    return gameVideoRepository.findDistinctTitles();
  }

  public Map<Integer, String> getGameVideoMap() {
    return gameVideoRepository.findAllByOrderByTitleAscIdAsc().stream()
        .collect(Collectors.toMap(video -> Math.toIntExact(video.getId()),
            GameVideoEntity::getTitle, (first, ignored) -> first, LinkedHashMap::new));
  }

  public List<String> getGameVideoNames(String title) {
    return gameVideoRepository.findDistinctNamesByTitle(normalize(title));
  }

  public GameVideoForm getGameVideo(Long id) {
    GameVideoEntity video = gameVideoRepository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game video not found"));
    return toForm(video);
  }

  private GameVideoForm toForm(GameVideoEntity video) {
    String videoUrl = UriComponentsBuilder.fromUriString(fileServerBaseUrl).path(videosPath)
        .pathSegment(video.getFileName()).encode(StandardCharsets.UTF_8).toUriString();
    return new GameVideoForm(video.getId(), video.getTitle(), video.getName(), video.getFileName(),
        video.getCreatedAt(), video.getUpdatedAt(), videoUrl);
  }

  private String normalize(String value) {
    return StringUtils.hasText(value) ? value.trim() : null;
  }
}
