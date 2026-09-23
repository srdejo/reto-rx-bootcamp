package com.pragma.bootcamp.application.handler;

import com.pragma.bootcamp.application.dto.request.BootcampRequestDto;
import com.pragma.bootcamp.application.dto.response.BootcampResponseDto;
import com.pragma.bootcamp.application.dto.response.PagedResponseDto;
import reactor.core.publisher.Mono;

public interface IBootcampHandler {

    Mono<Void> saveBootcamp(BootcampRequestDto bootcampRequestDto);

    Mono<PagedResponseDto<BootcampResponseDto>> getAllBootcamps(int page, int size, String sortBy, String direction);

    Mono<Void> deleteBootcamp(Long id);

    Mono<BootcampResponseDto> getBootcampById(Long id);
}
