package com.pragma.bootcamp.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BootcampRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    private Integer durationDays;

    @NotNull
    private List<Long> capacitiesIds;
}
