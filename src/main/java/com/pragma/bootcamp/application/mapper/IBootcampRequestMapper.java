package com.pragma.bootcamp.application.mapper;

import com.pragma.bootcamp.application.dto.request.BootcampRequestDto;
import com.pragma.bootcamp.domain.model.BootcampModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampRequestMapper {
    BootcampModel toBootcamp(BootcampRequestDto bootcampRequestDto);
}
