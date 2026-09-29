package arpa.home.hms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "business_model_canvas")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessModelCanvasEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "business_id", nullable = false, unique = true)
  private BusinessEntity business;

  @Column(columnDefinition = "TEXT")
  private String customerSegments;

  @Column(columnDefinition = "TEXT")
  private String valueProposition;

  @Column(columnDefinition = "TEXT")
  private String channels;

  @Column(columnDefinition = "TEXT")
  private String customerRelationships;

  @Column(columnDefinition = "TEXT")
  private String revenueStreams;

  @Column(columnDefinition = "TEXT")
  private String keyResources;

  @Column(columnDefinition = "TEXT")
  private String keyActivities;

  @Column(columnDefinition = "TEXT")
  private String keyPartners;

  @Column(columnDefinition = "TEXT")
  private String costStructure;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @CreatedBy
  @Column(updatable = false, nullable = false)
  private Integer createdBy;

  @LastModifiedBy
  @Column(nullable = false)
  private Integer updatedBy;

  @Version
  private Integer version;
}
