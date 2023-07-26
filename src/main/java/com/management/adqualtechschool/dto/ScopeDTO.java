package com.management.adqualtechschool.dto;

import com.management.adqualtechschool.entity.Event;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ScopeDTO {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Event event;
}
