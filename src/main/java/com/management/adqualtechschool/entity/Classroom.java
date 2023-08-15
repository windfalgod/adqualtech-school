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

    @OneToMany(mappedBy = "classRoom")
    private List<Account> pupils;
}
