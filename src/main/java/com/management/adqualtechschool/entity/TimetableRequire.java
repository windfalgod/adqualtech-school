package com.management.adqualtechschool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "timetable_require")
public class TimetableRequire extends BaseEntity{

    // the requirement for arrangement
    @Column(name = "name")
    private String name;
}
