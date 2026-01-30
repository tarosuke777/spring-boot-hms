package com.tarosuke777.hms.form;

import java.io.Serializable;
import com.tarosuke777.hms.validation.DeleteGroup;
import com.tarosuke777.hms.validation.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskForm implements Serializable {

    @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
    private Integer id;

    @NotBlank
    @Size(max = 255)
    private String name;

    private String note;

    @NotNull(groups = UpdateGroup.class)
    private Integer version;
}
