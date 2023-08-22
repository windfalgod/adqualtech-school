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
@Table(name = "elective_subject")
public class ElectiveSubject extends BaseEntity {

    // Classroom choose subject
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Classroom classroom;

    // Subject that is elective subject
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;
}
