package com.management.adqualtechschool.entity;

import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
@Table(name = "subject")
public class Subject extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    // Number unit of a subject
    @Column(name = "unit", nullable = false)
    private Long unit;

    // status of obligation or elective
    @Column(name = "status")
    private String status;

    // Combination of course
    @Column(name = "combination")
    private String combination;

    // List subject exam
    @OneToMany(mappedBy = "subject")
    private List<PrepareExam> prepareExams;

    // Set teacher teach this subject
    @OneToMany(mappedBy = "subject")
    private List<TeachSubject> teachSubjectList;

    // List elective subject
    @OneToMany(mappedBy = "subject")
    private List<ElectiveSubject> electiveSubjectList ;
}
