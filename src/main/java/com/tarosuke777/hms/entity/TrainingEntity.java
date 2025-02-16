package com.tarosuke777.hms.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class TrainingEntity {

	private Integer trainingId;
	private LocalDate trainingDate;
	private TrainingMenuEntity trainingMenu;
	private Integer weight;
	private Integer reps;
	private Integer sets;

}
