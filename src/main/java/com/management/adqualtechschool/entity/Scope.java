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

    // Tên phạm vi
    @Column(name = "title", nullable = false)
    private String title;

    // Danh sách sự kiện có phạm vi này
    @OneToMany(mappedBy = "scope")
    private List<Event> events;

    // Danh sách thông báo có phạm vi này
    @OneToMany(mappedBy = "scope")
    private List<Notify> notifies;

    @OneToMany(mappedBy = "scope")
    private List<Rule> rules;

    @OneToMany(mappedBy = "scope")
    private List<TeachSubject> teachSubjectList;
}
