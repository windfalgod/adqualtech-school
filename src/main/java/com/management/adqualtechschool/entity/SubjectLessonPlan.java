package com.management.adqualtechschool.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "subject_lesson_plan")
public class SubjectLessonPlan extends BaseEntity {

    // Subject and the person teach this subject
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_subject_id", referencedColumnName = "id")
    private TeachSubject teachSubject;

    // Classroom
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Classroom classroom;

    // time that subject occurred
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_plan_id", referencedColumnName = "id")
    private LessonPlan lessonPlan;

    // the study room that lesson take place
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "study_room_id", referencedColumnName = "id")
    private StudyRoom studyRoom;
}
