package com.pragma.bootcamp.domain.spi;

import com.pragma.bootcamp.domain.model.BootcampCapacities;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityClientPort {
    Mono<Void> associateCapacities(Long bootcampId, List<Long> capacityIds);

    Flux<BootcampCapacities> getCapacitiesByBootcampIds(List<Long> bootcampIds);

    Mono<Void> deleteCapacitiesByBootcampId(Long id);
}
