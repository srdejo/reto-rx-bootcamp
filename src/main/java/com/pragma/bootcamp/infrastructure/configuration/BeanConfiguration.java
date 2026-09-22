package com.pragma.bootcamp.infrastructure.configuration;

import com.pragma.bootcamp.domain.api.IBootcampServicePort;
import com.pragma.bootcamp.domain.spi.IBootcampPersistencePort;
import com.pragma.bootcamp.domain.spi.ICapacityClientPort;
import com.pragma.bootcamp.domain.usecase.BootcampUseCase;
import com.pragma.bootcamp.infrastructure.out.r2dbc.adapter.BootcampAdapter;
import com.pragma.bootcamp.infrastructure.out.r2dbc.mapper.IBootcampEntityMapper;
import com.pragma.bootcamp.infrastructure.out.r2dbc.repository.IBootcampRepository;
import com.pragma.bootcamp.infrastructure.out.webclient.adapter.CapacityWebClientAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper bootcampEntityMapper;

    @Value("${webclient.capacity}")
    private String capacityUrl;

    @Bean
    public IBootcampPersistencePort bootcampPersistencePort() {
        return new BootcampAdapter(bootcampRepository, bootcampEntityMapper);
    }

    @Bean
    public IBootcampServicePort bootcampServicePort() {
        return new BootcampUseCase(bootcampPersistencePort(), capacityClientPort());
    }

    @Bean
    public ICapacityClientPort capacityClientPort() {
        return new CapacityWebClientAdapter(WebClient.builder(), capacityUrl);
    }
}
