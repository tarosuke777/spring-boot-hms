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
public class DiaryForm {

    @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
    private Integer diaryId;

    @NotNull
    private LocalDate diaryDate;

    private String todoPlan;

    private String todoActual;

    private Integer funPlan;

    private Integer funActual;

    private String commentPlan;

    private String commentActual;

    @NotNull(groups = UpdateGroup.class)
    private Integer version;
}
