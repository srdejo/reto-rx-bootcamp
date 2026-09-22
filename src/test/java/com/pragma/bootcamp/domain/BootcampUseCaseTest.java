package com.pragma.bootcamp.domain;

import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.spi.IBootcampPersistencePort;
import com.pragma.bootcamp.domain.spi.ICapacityClientPort;
import com.pragma.bootcamp.domain.usecase.BootcampUseCase;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BootcampUseCaseTest {

    private final IBootcampPersistencePort port = mock(IBootcampPersistencePort.class);
    private final ICapacityClientPort capacityClientPort = mock(ICapacityClientPort.class);
    private final BootcampUseCase useCase = new BootcampUseCase(port, capacityClientPort);

    @Test
    void saveBootcampDelegatesToPort() {
        BootcampModel model = new BootcampModel(null, "test", "description", LocalDate.now(), 30);
        List<Long> capacityIds = List.of(1L);

        when(port.saveBootcamp(model)).thenReturn(Mono.just(model));
        when(capacityClientPort.associateCapacities(model.getId(), capacityIds)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.saveBootcamp(model, capacityIds)).expectNext(model).verifyComplete();
    }

    @Test
    void getAllBootcampsReturnsFlux() {
        BootcampModel model = new BootcampModel(1L, "test", "description", LocalDate.now(), 30);
        when(port.getAllBootcamps()).thenReturn(Flux.just(model));
        when(capacityClientPort.getCapacitiesByBootcampIds(List.of(1L))).thenReturn(Flux.empty());

        StepVerifier.create(useCase.getAllBootcamps()).expectNext(model).verifyComplete();
    }
}
