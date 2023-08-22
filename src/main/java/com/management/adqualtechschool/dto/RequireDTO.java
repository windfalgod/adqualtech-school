package com.management.adqualtechschool.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RequireDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
