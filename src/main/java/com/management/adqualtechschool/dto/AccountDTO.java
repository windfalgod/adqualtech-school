package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Classroom;
import com.management.adqualtechschool.entity.Subject;
import java.time.LocalDateTime;
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
    private Classroom classRoom;
    private String address;
    private String phone;
    private String email;
    private String level;
    private String position;
    private String rank;
    private Set<Subject> subjects;
    private Long classInChargedId;
}
