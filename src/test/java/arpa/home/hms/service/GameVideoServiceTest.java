package arpa.home.hms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import arpa.home.hms.entity.GameVideoEntity;
import arpa.home.hms.repository.GameVideoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class GameVideoServiceTest {

  @Mock
  private GameVideoRepository gameVideoRepository;

  @Test
  void getGameVideoPage_MapsReadOnlyRecordsAndEncodesTheFileName() {
    Pageable pageable = PageRequest.of(0, 10);
    GameVideoEntity video = new GameVideoEntity(1L, "Game", null, "recording one.mp4",
        LocalDateTime.parse("2026-10-03T10:00:00"), LocalDateTime.parse("2026-10-03T10:00:00"));
    when(gameVideoRepository.findAllByOrderByCreatedAtDescIdDesc(pageable))
        .thenReturn(new PageImpl<>(List.of(video), pageable, 1));
    GameVideoService service =
        new GameVideoService(gameVideoRepository, "http://192.168.10.199:8080/", "videos/");

    var result = service.getGameVideoPage(pageable);

    assertEquals(1, result.getTotalElements());
    assertEquals("http://192.168.10.199:8080/videos/recording%20one.mp4",
        result.getContent().getFirst().videoUrl());
    assertEquals("Game", result.getContent().getFirst().title());
    verify(gameVideoRepository).findAllByOrderByCreatedAtDescIdDesc(pageable);
  }

  @Test
  void getGameVideo_WhenMissing_ReturnsNotFound() {
    when(gameVideoRepository.findById(100L)).thenReturn(Optional.empty());
    GameVideoService service =
        new GameVideoService(gameVideoRepository, "http://192.168.10.199:8080/", "videos/");

    ResponseStatusException exception =
        assertThrows(ResponseStatusException.class, () -> service.getGameVideo(100L));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }
}
