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
@Table(name = "scope")
public class Scope extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "scope")
    private List<Event> events;

    @OneToMany(mappedBy = "scope")
    private List<Notify> notifies;
}
