package com.pragma.bootcamp.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CapacitySummaryResponseDto {
    private Long id;
    private String name;
    private String description;
    private List<TechnologySummaryResponseDto> technologies;
}
