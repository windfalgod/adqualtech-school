package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.SubjectLessonPlan;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class StudyRoomDTO {
    private Long id;
    private String name;
    private String description;
    private List<SubjectLessonPlan> subjectLessonPlanList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
