package com.tarosuke777.hms.form;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainingForm {
	private Integer trainingId;

	private LocalDate trainingDate;

	private Integer trainingMenuId;

	private Integer weight;

	private Integer reps;

}
