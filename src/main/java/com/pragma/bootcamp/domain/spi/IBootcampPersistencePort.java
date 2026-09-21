package com.pragma.bootcamp.domain.spi;

import com.pragma.bootcamp.domain.model.BootcampModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {
    Mono<BootcampModel> saveBootcamp(BootcampModel bootcampModel);

    Flux<BootcampModel> getAllBootcamps();
}
