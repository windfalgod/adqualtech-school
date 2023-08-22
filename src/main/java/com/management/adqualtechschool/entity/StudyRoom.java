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
@Table(name = "study_room")
public class StudyRoom extends BaseEntity{

    // Tên phòng học
    @Column(name = "name", nullable = false)
    private String name;

    // Mô tả về phòng học
    @Column(name = "description")
    private String description;

    // Danh sách môn học và thời gian dạy
    @OneToMany(mappedBy = "studyRoom")
    private List<SubjectLessonPlan> subjectLessonPlanList;
}
