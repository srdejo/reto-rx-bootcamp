package com.pragma.bootcamp.domain.api;

import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.util.PagedResult;
import com.pragma.bootcamp.domain.util.enums.BootcampSortBy;
import com.pragma.bootcamp.domain.util.enums.SortDirection;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampServicePort {

    Mono<BootcampModel> saveBootcamp(BootcampModel bootcampModel, List<Long> capacityIds);

    Mono<PagedResult<BootcampModel>> getAllBootcamps(int page, int size, BootcampSortBy sortBy, SortDirection direction);

    Mono<Void> deleteBootcamp(Long id);
}
