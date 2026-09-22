package com.pragma.bootcamp.domain.model;

import java.util.List;

public record BootcampCapacities(
        Long bootcampId,
        List<CapacityModel> capacities
) {
}
