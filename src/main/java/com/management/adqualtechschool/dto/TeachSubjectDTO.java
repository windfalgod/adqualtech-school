package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Subject;
import com.management.adqualtechschool.entity.SubjectLessonPlan;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TeachSubjectDTO {
    private Long id;
    private Account teacher;
    private Subject subject;
    private List<SubjectLessonPlan> subjectLessonPlanList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
