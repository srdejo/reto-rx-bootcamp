package com.pragma.bootcamp.application.handler.impl;

import com.pragma.bootcamp.application.dto.request.BootcampRequestDto;
import com.pragma.bootcamp.application.dto.response.BootcampResponseDto;
import com.pragma.bootcamp.application.dto.response.PagedResponseDto;
import com.pragma.bootcamp.application.handler.IBootcampHandler;
import com.pragma.bootcamp.application.mapper.IBootcampRequestMapper;
import com.pragma.bootcamp.application.mapper.IBootcampResponseMapper;
import com.pragma.bootcamp.domain.api.IBootcampServicePort;
import com.pragma.bootcamp.domain.exception.InvalidPaginationParameterException;
import com.pragma.bootcamp.domain.util.enums.BootcampSortBy;
import com.pragma.bootcamp.domain.util.enums.SortDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BootcampHandler implements IBootcampHandler {

    private final IBootcampServicePort bootcampServicePort;
    private final IBootcampRequestMapper bootcampRequestMapper;
    private final IBootcampResponseMapper bootcampResponseMapper;

    @Override
    public Mono<Void> saveBootcamp(BootcampRequestDto bootcampRequestDto) {
        return bootcampServicePort.saveBootcamp(
                bootcampRequestMapper.toBootcamp(bootcampRequestDto),
                bootcampRequestDto.getCapacitiesIds()
        ).then();
    }

    @Override
    public Mono<PagedResponseDto<BootcampResponseDto>> getAllBootcamps(int page, int size, String sortBy, String direction) {
        BootcampSortBy bootcampSortBy = parseEnum(BootcampSortBy.class, toEnumName(sortBy));
        SortDirection sortDirection = parseEnum(SortDirection.class, toEnumName(direction));

        return bootcampServicePort.getAllBootcamps(page, size, bootcampSortBy, sortDirection)
                .map(pagedResult -> new PagedResponseDto<>(
                        pagedResult.content().stream().map(bootcampResponseMapper::toResponse).toList(),
                        pagedResult.page(),
                        pagedResult.size(),
                        pagedResult.totalElements(),
                        pagedResult.totalPages()
                ));
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumType, String value) {
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException _) {
            throw new InvalidPaginationParameterException();
        }
    }

    private String toEnumName(String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1_$2")
                .replace("-", "_")
                .toUpperCase();
    }
}
