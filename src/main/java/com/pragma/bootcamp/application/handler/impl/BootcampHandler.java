package com.pragma.bootcamp.application.handler.impl;

import com.pragma.bootcamp.application.dto.request.BootcampRequestDto;
import com.pragma.bootcamp.application.dto.response.BootcampResponseDto;
import com.pragma.bootcamp.application.handler.IBootcampHandler;
import com.pragma.bootcamp.application.mapper.IBootcampRequestMapper;
import com.pragma.bootcamp.application.mapper.IBootcampResponseMapper;
import com.pragma.bootcamp.domain.api.IBootcampServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BootcampHandler implements IBootcampHandler {

    private final IBootcampServicePort bootcampServicePort;
    private final IBootcampRequestMapper bootcampRequestMapper;
    private final IBootcampResponseMapper bootcampResponseMapper;

    @Override
    public Mono<Void> saveBootcamp(BootcampRequestDto bootcampRequestDto) {
        return bootcampServicePort.saveBootcamp(bootcampRequestMapper.toBootcamp(bootcampRequestDto)).then();
    }

    @Override
    public Flux<BootcampResponseDto> getAllBootcamps() {
        return bootcampServicePort.getAllBootcamps().map(bootcampResponseMapper::toResponse);
    }
}
