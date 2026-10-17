package arpa.home.hms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "v_game_videos")
@Immutable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameVideoEntity {

  @Id
  private Long id;

  @Column(nullable = false)
  private String title;

  private String name;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
