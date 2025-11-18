package com.tarosuke777.hms.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class DiaryEntity {

    private Integer diaryId;
    private LocalDate diaryDate;
    private String todoPlan;
    private String todoActual;
    private Integer funPlan;
    private Integer funActual;
    private String commentPlan;
    private String commentActual;

}
