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

    @Column(name = "unit", nullable = false)
    private Long unit;

    @OneToMany(mappedBy = "subject")
    private List<PrepareExam> prepareExams;

    @ManyToMany(mappedBy = "subjects")
    private Set<Account> accounts;
}
