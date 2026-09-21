package com.pragma.bootcamp.application.handler;

import com.pragma.bootcamp.application.dto.request.BootcampRequestDto;
import com.pragma.bootcamp.application.dto.response.BootcampResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampHandler {

    Mono<Void> saveBootcamp(BootcampRequestDto bootcampRequestDto);

    Flux<BootcampResponseDto> getAllBootcamps();
}
