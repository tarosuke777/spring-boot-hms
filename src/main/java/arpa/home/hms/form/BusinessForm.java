package arpa.home.hms.form;

import arpa.home.hms.enums.BusinessGenre;
import arpa.home.hms.enums.BusinessStatus;
import arpa.home.hms.validation.DeleteGroup;
import arpa.home.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldNameConstants
public class BusinessForm {

  @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
  private Integer id;

  @NotBlank
  @Size(max = 255)
  private String name;

  @NotNull(message = "ジャンルを選択してください")
  private BusinessGenre genre;

  @NotNull(message = "ステータスを選択してください")
  private BusinessStatus status;

  @NotBlank
  @Size(max = 2000)
  private String overview;

  @Size(max = 2000)
  private String customerSegments;

  @Size(max = 2000)
  private String valueProposition;

  @Size(max = 2000)
  private String channels;

  @Size(max = 2000)
  private String customerRelationships;

  @Size(max = 2000)
  private String revenueStreams;

  @Size(max = 2000)
  private String keyResources;

  @Size(max = 2000)
  private String keyActivities;

  @Size(max = 2000)
  private String keyPartners;

  @Size(max = 2000)
  private String costStructure;

  private LocalDateTime updatedAt;

  @NotNull(groups = UpdateGroup.class)
  private Integer version;
}
