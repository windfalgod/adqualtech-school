package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.ElectiveSubject;
import com.management.adqualtechschool.entity.PrepareExam;
import com.management.adqualtechschool.entity.TeachSubject;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class SubjectDTO {
    private Long id;
    private String name;
    private Integer unit;
    private String combination;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PrepareExam> prepareExams;
    private List<Account> accounts;
    private List<TeachSubject> teachSubjectList;
    private List<ElectiveSubject> electiveSubjectList;
}
