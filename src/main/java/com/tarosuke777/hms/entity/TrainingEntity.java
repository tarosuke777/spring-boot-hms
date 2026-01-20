package com.tarosuke777.hms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trainingId;

	private LocalDate trainingDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "training_menu_id")
	private TrainingMenuEntity trainingMenu;

	private Integer weight;

	private Integer reps;

	private Integer sets;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@CreatedBy
	@Column(updatable = false, nullable = false)
	private String createdBy;

	@LastModifiedBy
	@Column(nullable = false)
	private String updatedBy;

	@Version
	private Integer version;
}
