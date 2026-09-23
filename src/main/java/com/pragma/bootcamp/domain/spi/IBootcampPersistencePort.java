package com.pragma.bootcamp.domain.spi;

import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.util.enums.SortDirection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {
    Mono<BootcampModel> saveBootcamp(BootcampModel bootcampModel);

    Flux<BootcampModel> getAllBootcamps();

    Flux<BootcampModel> getBootcampsPageSortedByName(int page, int size, SortDirection direction);

    Mono<Long> countBootcamps();
}
