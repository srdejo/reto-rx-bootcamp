package com.pragma.bootcamp.domain.usecase;

import com.pragma.bootcamp.domain.api.IBootcampServicePort;
import com.pragma.bootcamp.domain.exception.DuplicateCapacityException;
import com.pragma.bootcamp.domain.exception.InvalidCapacityCountException;
import com.pragma.bootcamp.domain.model.BootcampCapacities;
import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.model.CapacityModel;
import com.pragma.bootcamp.domain.spi.IBootcampPersistencePort;
import com.pragma.bootcamp.domain.spi.ICapacityClientPort;
import com.pragma.bootcamp.domain.util.DomainConstants;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

@Transactional
public class BootcampUseCase implements IBootcampServicePort {

    private final IBootcampPersistencePort bootcampPersistencePort;
    private final ICapacityClientPort capacityClientPort;

    public BootcampUseCase(IBootcampPersistencePort bootcampPersistencePort, ICapacityClientPort capacityClientPort) {
        this.bootcampPersistencePort = bootcampPersistencePort;
        this.capacityClientPort = capacityClientPort;
    }

    @Override
    public Mono<BootcampModel> saveBootcamp(BootcampModel bootcampModel, List<Long> capacityIds) {
        validateCapacities(capacityIds);

        return bootcampPersistencePort.saveBootcamp(bootcampModel)
                .flatMap(savedBootcamp ->
                        capacityClientPort
                                .associateCapacities(savedBootcamp.getId(), capacityIds)
                                .thenReturn(savedBootcamp)
                );
    }

    @Override
    public Flux<BootcampModel> getAllBootcamps() {
        return bootcampPersistencePort.getAllBootcamps()
                .collectList()
                .flatMapMany(this::attachCapacities);
    }

    private void validateCapacities(List<Long> capacityIds) {
        if (capacityIds.size() < DomainConstants.MIN_CAPACITIES
                || capacityIds.size() > DomainConstants.MAX_CAPACITIES) {
            throw new InvalidCapacityCountException();
        }

        if (capacityIds.size() != new HashSet<>(capacityIds).size()) {
            throw new DuplicateCapacityException();
        }
    }

    private Flux<BootcampModel> attachCapacities(List<BootcampModel> bootcamps) {
        List<Long> bootcampIds = bootcamps.stream().map(BootcampModel::getId).toList();
        if (bootcampIds.isEmpty()) {
            return Flux.fromIterable(bootcamps);
        }

        return capacityClientPort.getCapacitiesByBootcampIds(bootcampIds)
                .collectMap(BootcampCapacities::bootcampId, BootcampCapacities::capacities)
                .flatMapMany(capacitiesByBootcampId -> {
                    bootcamps.forEach(bootcamp -> {
                        List<CapacityModel> capacities = capacitiesByBootcampId.getOrDefault(bootcamp.getId(), List.of());
                        bootcamp.setCapacities(capacities);
                    });
                    return Flux.fromIterable(bootcamps);
                });
    }
}
