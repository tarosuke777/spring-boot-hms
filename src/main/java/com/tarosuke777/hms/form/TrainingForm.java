package com.tarosuke777.hms.form;

import java.time.LocalDate;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainingForm {

	@NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
	private Integer trainingId;

	private LocalDate trainingDate;

	private Integer trainingAreaId;

	private Integer trainingMenuId;

	private Integer weight;

	private Integer reps;

	private Integer sets;

	@NotNull(groups = UpdateGroup.class)
	private Integer version;
}
