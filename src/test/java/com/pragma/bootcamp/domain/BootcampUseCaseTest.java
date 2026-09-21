package com.pragma.bootcamp.domain;

import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.spi.IBootcampPersistencePort;
import com.pragma.bootcamp.domain.usecase.BootcampUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class BootcampUseCaseTest {

    private final IBootcampPersistencePort port = Mockito.mock(IBootcampPersistencePort.class);
    private final BootcampUseCase useCase = new BootcampUseCase(port);

    @Test
    void saveBootcampDelegatesToPort() {
        BootcampModel model = new BootcampModel(null, "test");
        Mockito.when(port.saveBootcamp(model)).thenReturn(Mono.just(model));

        StepVerifier.create(useCase.saveBootcamp(model)).expectNext(model).verifyComplete();
    }

    @Test
    void getAllBootcampsReturnsFlux() {
        BootcampModel model = new BootcampModel(null, "test");
        Mockito.when(port.getAllBootcamps()).thenReturn(Flux.just(model));

        StepVerifier.create(useCase.getAllBootcamps()).expectNext(model).verifyComplete();
    }
}
