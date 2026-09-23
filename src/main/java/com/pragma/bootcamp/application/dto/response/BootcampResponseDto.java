package com.pragma.bootcamp.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BootcampResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer durationDays;
    private List<CapacitySummaryResponseDto> capacities;
}
