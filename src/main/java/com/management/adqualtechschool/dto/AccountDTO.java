package com.management.adqualtechschool.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AccountDTO {
    private Long id;
    private String firsName;
    private String lastName;
    private Boolean gender;
    private LocalDateTime birthday;
    private String image;
    private Long classId;
    private String address;
    private String phone;
    private String email;
    private String level;
    private Long classInChargedId;
}
