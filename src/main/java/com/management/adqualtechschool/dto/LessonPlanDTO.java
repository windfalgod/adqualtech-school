package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.SubjectLessonPlan;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class LessonPlanDTO {
    private Long id;
    private Integer lessonOfDay;
    private Integer dayOfWeek;
    private List<SubjectLessonPlan> subjectLessonPlanList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
