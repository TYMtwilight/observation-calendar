package com.example.observation_calendar.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ObservationRecordDto {
    private Long id;
    private Long project_id;
    private LocalDateTime observationDate;
    private String memo;
    private String weather;
    private BigDecimal temperature;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PhotoDto> photos;

}
