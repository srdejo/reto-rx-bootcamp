package com.pragma.bootcamp.infrastructure.configuration;

import com.pragma.bootcamp.domain.api.IBootcampServicePort;
import com.pragma.bootcamp.domain.spi.IBootcampPersistencePort;
import com.pragma.bootcamp.domain.usecase.BootcampUseCase;
import com.pragma.bootcamp.infrastructure.out.r2dbc.adapter.BootcampAdapter;
import com.pragma.bootcamp.infrastructure.out.r2dbc.mapper.IBootcampEntityMapper;
import com.pragma.bootcamp.infrastructure.out.r2dbc.repository.IBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper bootcampEntityMapper;

    @Bean
    public IBootcampPersistencePort bootcampPersistencePort() {
        return new BootcampAdapter(bootcampRepository, bootcampEntityMapper);
    }

    @Bean
    public IBootcampServicePort bootcampServicePort() {
        return new BootcampUseCase(bootcampPersistencePort());
    }
}
