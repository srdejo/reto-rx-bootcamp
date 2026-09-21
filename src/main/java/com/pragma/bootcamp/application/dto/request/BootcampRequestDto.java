package com.pragma.bootcamp.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BootcampRequestDto {
    @NotBlank
    @Size(max = 50)
    private String name;
}
