package com.pragma.bootcamp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BootcampModel {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer durationDays;
    private List<CapacityModel> capacities;

    public BootcampModel(Long id, String name, String description, LocalDate releaseDate, Integer durationDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.durationDays = durationDays;
    }
}
