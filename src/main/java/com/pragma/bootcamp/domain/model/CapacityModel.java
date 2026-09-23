package com.pragma.bootcamp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapacityModel {
    private Long id;
    private String name;
    private String description;
    private List<TechnologyModel> technologies;

    public CapacityModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
