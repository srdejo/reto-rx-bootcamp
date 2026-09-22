package com.pragma.bootcamp.application.mapper;

import com.pragma.bootcamp.application.dto.response.BootcampResponseDto;
import com.pragma.bootcamp.application.dto.response.CapacitySummaryResponseDto;
import com.pragma.bootcamp.domain.model.BootcampModel;
import com.pragma.bootcamp.domain.model.CapacityModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampResponseMapper {
    BootcampResponseDto toResponse(BootcampModel bootcampModel);

    CapacitySummaryResponseDto toResponse(CapacityModel capacityModel);
}
