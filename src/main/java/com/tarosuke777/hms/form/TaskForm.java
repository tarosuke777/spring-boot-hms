package com.tarosuke777.hms.form;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskForm implements Serializable {
    private Integer id;

    @NotBlank
    @Size(max = 255)
    private String name;

    private String note;

    private Integer version;
}
