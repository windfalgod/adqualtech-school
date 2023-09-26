package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AttendanceDTO {
    private Long id;
    private Account account;
    private String content;
    private Integer date;
    private Integer month;
    private Integer year;
    private Integer dayOfWeek;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
