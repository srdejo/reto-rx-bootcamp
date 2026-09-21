package com.pragma.bootcamp.domain.usecase;

import com.pragma.bootcamp.domain.api.IBootcampServicePort;
import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.spi.IBootcampPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BootcampUseCase implements IBootcampServicePort {

    private final IBootcampPersistencePort bootcampPersistencePort;

    public BootcampUseCase(IBootcampPersistencePort bootcampPersistencePort) {
        this.bootcampPersistencePort = bootcampPersistencePort;
    }

    @Override
    public Mono<BootcampModel> saveBootcamp(BootcampModel bootcampModel) {
        return bootcampPersistencePort.saveBootcamp(bootcampModel);
    }

    @Override
    public Flux<BootcampModel> getAllBootcamps() {
        return bootcampPersistencePort.getAllBootcamps();
    }
}
