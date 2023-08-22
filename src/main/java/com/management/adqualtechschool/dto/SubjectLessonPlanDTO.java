package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Classroom;
import com.management.adqualtechschool.entity.LessonPlan;
import com.management.adqualtechschool.entity.StudyRoom;
import com.management.adqualtechschool.entity.TeachSubject;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SubjectLessonPlanDTO {
    private Long id;
    private TeachSubject teachSubject;
    private Classroom classroom;
    private LessonPlan lessonPlan;
    private StudyRoom studyRoom;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
