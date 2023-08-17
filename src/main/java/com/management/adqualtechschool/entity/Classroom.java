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
@Table(name = "class")
public class Classroom extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    // List pupils in class
    @OneToMany(mappedBy = "classroom")
    private List<Account> pupils;

    // List subject lesson in class
    @OneToMany(mappedBy = "classroom")
    private List<SubjectLessonPlan> subjectLessonPlanList;

    // List elective subject
    @OneToMany(mappedBy = "classroom")
    private List<ElectiveSubject> electiveSubjectList;
}
