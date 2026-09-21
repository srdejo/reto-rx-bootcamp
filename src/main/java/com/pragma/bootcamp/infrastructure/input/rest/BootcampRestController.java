package com.pragma.bootcamp.infrastructure.input.rest;

import com.pragma.bootcamp.application.dto.request.BootcampRequestDto;
import com.pragma.bootcamp.application.dto.response.BootcampResponseDto;
import com.pragma.bootcamp.application.handler.IBootcampHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bootcamp")
@RequiredArgsConstructor
public class BootcampRestController {

    private final IBootcampHandler bootcampHandler;

    @Operation(summary = "Add a new bootcamp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bootcamp created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping("/")
    public Mono<ResponseEntity<Void>> saveBootcamp(@Valid @RequestBody BootcampRequestDto bootcampRequestDto) {
        return bootcampHandler.saveBootcamp(bootcampRequestDto)
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    }

    @Operation(summary = "Get all bootcamps")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All bootcamps returned",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BootcampResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping("/")
    public Flux<BootcampResponseDto> getAllBootcamps() {
        return bootcampHandler.getAllBootcamps();
    }
}
