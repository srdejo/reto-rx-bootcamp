package com.pragma.bootcamp.domain.api;

import com.pragma.bootcamp.domain.model.BootcampModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {

    Mono<BootcampModel> saveBootcamp(BootcampModel bootcampModel);

    Flux<BootcampModel> getAllBootcamps();
}
