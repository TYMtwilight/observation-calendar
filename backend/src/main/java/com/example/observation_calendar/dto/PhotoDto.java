package com.example.observation_calendar.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PhotoDto {
    private Long id;
    private Long recordId;
    private String cloudinaryPublicId;
    private String cloudinaryUrl;
    private String originalFileName;
    private Long fileSize;
    private Integer uploadOrder;
    private LocalDateTime createdAt;
}
