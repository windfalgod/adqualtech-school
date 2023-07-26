package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Account;
import com.management.adqualtechschool.entity.Scope;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventDTO {
    private Long id;
    private String title;
    private String content;
    private String image;
    private Account creator;
    private Scope scope;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
