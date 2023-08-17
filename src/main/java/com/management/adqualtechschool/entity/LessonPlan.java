package com.management.adqualtechschool.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lesson_plan")
public class LessonPlan extends BaseEntity {

    // lesson in day
    @Column(name = "lesson_of_day", nullable = false)
    private Integer lessonOfDay;

    // the day of week
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    // Get Subject and Class
    @OneToMany(mappedBy = "lessonPlan")
    private List<SubjectLessonPlan> subjectLessonPlanList;
}
