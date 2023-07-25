package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Scope;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotifyDTO {
    private Long id;
    private String title;
    private String content;
    private Account creator;
    private Scope scope;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
