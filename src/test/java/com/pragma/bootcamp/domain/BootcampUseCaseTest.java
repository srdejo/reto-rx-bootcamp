package com.pragma.bootcamp.domain;

import com.pragma.bootcamp.domain.model.BootcampCapacities;
import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.model.CapacityModel;
import com.pragma.bootcamp.domain.model.TechnologyModel;
import com.pragma.bootcamp.domain.spi.IBootcampPersistencePort;
import com.pragma.bootcamp.domain.spi.ICapacityClientPort;
import com.pragma.bootcamp.domain.usecase.BootcampUseCase;
import com.pragma.bootcamp.domain.util.PagedResult;
import com.pragma.bootcamp.domain.util.enums.BootcampSortBy;
import com.pragma.bootcamp.domain.util.enums.SortDirection;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    // Criterio: se debe poder parametrizar el orden (ascendente/descendente) por nombre o por cantidad de capacidades.
    @Test
    void getAllBootcampsSortsByCapacityCountAscendingWhenRequested() {
        mockBootcampsWithCapacities();

        PagedResult<BootcampModel> result = useCase.getAllBootcamps(0, 10, BootcampSortBy.CAPACITY_COUNT, SortDirection.ASC)
                .block();

        assertEquals(List.of("Frontend Bootcamp", "Backend Bootcamp", "Fullstack Bootcamp"),
                result.content().stream().map(BootcampModel::getName).toList());
    }

    // Criterio: el servicio debe estar paginado.
    @Test
    void getAllBootcampsReturnsRequestedPage() {
        mockBootcampsPageSortedByName(List.of(
                bootcamp(1L, "Backend Bootcamp"),
                bootcamp(3L, "Data Bootcamp")), 3);

        PagedResult<BootcampModel> result = useCase.getAllBootcamps(0, 2, BootcampSortBy.NAME, SortDirection.ASC)
                .block();

        assertEquals(2, result.content().size());
        assertEquals(0, result.page());
        assertEquals(2, result.size());
        assertEquals(3, result.totalElements());
        assertEquals(2, result.totalPages());
    }

    // Criterio: cada bootcamp listado debe traer sus capacidades (id, nombre) con las tecnologias (id, nombre) de cada una.
    @Test
    void getAllBootcampsAttachesCapacitiesWithIdAndName() {
        mockBootcampsPageSortedByName(List.of(
                bootcamp(1L, "Backend Bootcamp"),
                bootcamp(3L, "Data Bootcamp")), 2);

        PagedResult<BootcampModel> result = useCase.getAllBootcamps(0, 10, BootcampSortBy.NAME, SortDirection.ASC)
                .block();

        BootcampModel backend = result.content().stream()
                .filter(bootcamp -> bootcamp.getId().equals(1L))
                .findFirst()
                .orElseThrow();

        assertEquals(
                List.of("10:Backend"),
                backend.getCapacities().stream()
                        .map(capacity -> capacity.getId() + ":" + capacity.getName())
                        .toList()
        );
    }

    @Test
    void getBootcampByIdAttachesCapacitiesWithTechnologies() {
        BootcampModel backend = bootcamp(1L, "Backend Bootcamp");
        CapacityModel capacity = new CapacityModel(10L, "Backend", "APIs y bases de datos",
                List.of(new TechnologyModel(100L, "Java")));

        when(port.getBootcampById(1L)).thenReturn(Mono.just(backend));
        when(capacityClientPort.getCapacitiesByBootcampIds(List.of(1L)))
                .thenReturn(Flux.just(new BootcampCapacities(1L, List.of(capacity))));

        StepVerifier.create(useCase.getBootcampById(1L))
                .assertNext(result -> {
                    assertEquals(1, result.getCapacities().size());
                    assertEquals("APIs y bases de datos", result.getCapacities().get(0).getDescription());
                    assertEquals("Java", result.getCapacities().get(0).getTechnologies().get(0).getName());
                })
                .verifyComplete();
    }

    private void mockBootcampsWithCapacities() {
        BootcampModel backend = bootcamp(1L, "Backend Bootcamp");
        BootcampModel frontend = bootcamp(2L, "Frontend Bootcamp");
        BootcampModel fullstack = bootcamp(3L, "Fullstack Bootcamp");

        when(port.getAllBootcamps()).thenReturn(Flux.just(backend, frontend, fullstack));

        when(capacityClientPort.getCapacitiesByBootcampIds(List.of(1L, 2L, 3L)))
                .thenReturn(Flux.just(
                        new BootcampCapacities(1L, List.of(new CapacityModel(10L, "Backend"))),
                        new BootcampCapacities(2L, List.of()),
                        new BootcampCapacities(3L, List.of(
                                new CapacityModel(10L, "Backend"),
                                new CapacityModel(11L, "Frontend")))
                ));
    }

    private void mockBootcampsPageSortedByName(List<BootcampModel> pageContent, long totalElements) {
        when(port.getBootcampsPageSortedByName(anyInt(), anyInt(), any(SortDirection.class)))
                .thenReturn(Flux.fromIterable(pageContent));
        when(port.countBootcamps()).thenReturn(Mono.just(totalElements));

        List<Long> ids = pageContent.stream().map(BootcampModel::getId).toList();
        when(capacityClientPort.getCapacitiesByBootcampIds(ids)).thenReturn(Flux.just(
                new BootcampCapacities(1L, List.of(new CapacityModel(10L, "Backend"))),
                new BootcampCapacities(3L, List.of())));
    }

    private BootcampModel bootcamp(Long id, String name) {
        return new BootcampModel(id, name, "description", LocalDate.now(), 30);
    }
}
