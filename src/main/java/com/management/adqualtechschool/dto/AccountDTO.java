package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Classroom;
import com.management.adqualtechschool.entity.Role;
import com.management.adqualtechschool.entity.Subject;
import com.management.adqualtechschool.entity.TeachSubject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class AccountDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean gender;
    private LocalDateTime birthday;
    private String image;
    private Classroom classroom;
    private String address;
    private String phone;
    private String email;
    private String level;
    private String position;
    private String rank;
    private Set<Role> roles;
    private List<TeachSubject> teachSubjectList;
    private Long classInChargedId;
}
