package com.tarosuke777.hms.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskEntity implements Serializable {
    private Integer id;
    private String name;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
