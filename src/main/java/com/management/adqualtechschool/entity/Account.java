package com.management.adqualtechschool.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "account")
public class Account extends BaseEntity {

    @Column(name = "username", length = 15, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 30, nullable = false)
    private String lastName;

    @Column(name = "gender", nullable = false)
    private Boolean gender;

    @Column(name = "birthday", nullable = false)
    private LocalDateTime birthday;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    private Classroom classroom;

    @Column(name = "address")
    private String address;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "email", length = 50)
    private String email;

    // Trình độ
    @Column(name = "level", length = 30)
    private String level;

    // Cấp bậc
    @Column(name = "rank", length = 50)
    private String rank;

    // Chức vụ
    @Column(name = "position", length = 50)
    private String position;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_in_charged_id", referencedColumnName = "id")
    private Classroom classInCharged;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // List event
    @OneToMany(mappedBy = "creator")
    private List<Event> events;

    // List notify
    @OneToMany(mappedBy = "creator")
    private List<Notify> notifies;

    // List rule
    @OneToMany(mappedBy = "creator")
    private List<Rule> rules;

    // Set teach subject
    @OneToMany(mappedBy = "teacher")
    private List<TeachSubject> teachSubjectList;
}
