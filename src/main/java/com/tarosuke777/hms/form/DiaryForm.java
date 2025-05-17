package com.tarosuke777.hms.form;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DiaryForm {
    private Integer diaryId;
    private LocalDate diaryDate;
    private String todoPlan;
    private String todoActual;
    private Integer funPlan;
    private Integer funActual;
    private String commentPlan;
    private String commentActual;
}