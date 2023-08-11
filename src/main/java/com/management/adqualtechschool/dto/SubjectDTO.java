package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.PrepareExam;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class SubjectDTO {
    private Long id;
    private String name;
    private Long unit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PrepareExam> prepareExams;
    private List<Account> accounts;
}
