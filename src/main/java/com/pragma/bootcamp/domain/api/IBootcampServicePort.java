package com.pragma.bootcamp.domain.api;

import com.pragma.bootcamp.domain.model.BootcampModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampServicePort {

    Mono<BootcampModel> saveBootcamp(BootcampModel bootcampModel, List<Long> capacityIds);

    Flux<BootcampModel> getAllBootcamps();
}
