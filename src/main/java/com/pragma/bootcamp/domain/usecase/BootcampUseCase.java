package com.pragma.bootcamp.domain.usecase;

import com.pragma.bootcamp.domain.api.IBootcampServicePort;
import com.pragma.bootcamp.domain.exception.DuplicateCapacityException;
import com.pragma.bootcamp.domain.exception.InvalidCapacityCountException;
import com.pragma.bootcamp.domain.exception.InvalidPaginationParameterException;
import com.pragma.bootcamp.domain.model.BootcampCapacities;
import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.spi.IBootcampPersistencePort;
import com.pragma.bootcamp.domain.spi.ICapacityClientPort;
import com.pragma.bootcamp.domain.util.DomainConstants;
import com.pragma.bootcamp.domain.util.PagedResult;
import com.pragma.bootcamp.domain.util.enums.BootcampSortBy;
import com.pragma.bootcamp.domain.util.enums.SortDirection;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import static com.pragma.bootcamp.domain.util.enums.BootcampSortBy.NAME;

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
    public Mono<PagedResult<BootcampModel>> getAllBootcamps(int page, int size, BootcampSortBy sortBy, SortDirection direction) {
        if (page < 0 || size < 1) {
            throw new InvalidPaginationParameterException();
        }

        return sortBy == NAME
                ? getPageSortedByName(page, size, direction)
                : getPageSortedByCapacityCount(page, size, direction);
    }

    @Override
    public Mono<Void> deleteBootcamp(Long id) {
        return capacityClientPort.deleteCapacitiesByBootcampId(id)
                .then(bootcampPersistencePort.deleteBootcamp(id));
    }

    @Override
    public Mono<BootcampModel> getBootcampById(Long id) {
        return bootcampPersistencePort.getBootcampById(id)
                .flatMap(bootcamp -> attachCapacities(List.of(bootcamp))
                        .map(List::getFirst));
    }

    private Mono<PagedResult<BootcampModel>> getPageSortedByName(int page, int size, SortDirection direction) {
        return bootcampPersistencePort.getBootcampsPageSortedByName(page, size, direction)
                .collectList()
                .zipWith(bootcampPersistencePort.countBootcamps())
                .flatMap(pageAndCount -> attachCapacities(pageAndCount.getT1())
                        .map(withCapacities -> toPagedResult(withCapacities, page, size, pageAndCount.getT2())));
    }

    private Mono<PagedResult<BootcampModel>> getPageSortedByCapacityCount(int page, int size, SortDirection direction) {
        return bootcampPersistencePort.getAllBootcamps()
                .collectList()
                .flatMap(bootcamps -> attachCapacities(bootcamps)
                        .map(withCapacities -> buildPageSortedByCapacityCount(withCapacities, page, size, direction)));
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

    private Mono<List<BootcampModel>> attachCapacities(List<BootcampModel> bootcamps) {
        List<Long> bootcampIds = bootcamps.stream().map(BootcampModel::getId).toList();
        if (bootcampIds.isEmpty()) {
            return Mono.just(bootcamps);
        }

        return capacityClientPort.getCapacitiesByBootcampIds(bootcampIds)
                .collectMap(BootcampCapacities::bootcampId, BootcampCapacities::capacities)
                .map(capacitiesByBootcampId -> {
                    bootcamps.forEach(bootcamp -> bootcamp.setCapacities(
                            capacitiesByBootcampId.getOrDefault(bootcamp.getId(), List.of())));
                    return bootcamps;
                });
    }

    private PagedResult<BootcampModel> buildPageSortedByCapacityCount(List<BootcampModel> bootcamps, int page, int size,
                                                                        SortDirection direction) {
        Comparator<BootcampModel> comparator = Comparator.comparingInt(bootcamp -> bootcamp.getCapacities().size());
        if (direction == SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        List<BootcampModel> sorted = bootcamps.stream().sorted(comparator).toList();
        long totalElements = sorted.size();

        List<BootcampModel> pageContent = sorted.stream()
                .skip((long) page * size)
                .limit(size)
                .toList();

        return toPagedResult(pageContent, page, size, totalElements);
    }

    private PagedResult<BootcampModel> toPagedResult(List<BootcampModel> pageContent, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PagedResult<>(pageContent, page, size, totalElements, totalPages);
    }
}
