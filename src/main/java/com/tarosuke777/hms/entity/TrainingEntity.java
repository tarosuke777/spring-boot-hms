package com.tarosuke777.hms.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training")
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
}
