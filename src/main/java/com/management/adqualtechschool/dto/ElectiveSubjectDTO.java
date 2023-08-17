package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Classroom;
import com.management.adqualtechschool.entity.Subject;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ElectiveSubjectDTO {
    private Long id;
    private Classroom classroom;
    private Subject subject;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
