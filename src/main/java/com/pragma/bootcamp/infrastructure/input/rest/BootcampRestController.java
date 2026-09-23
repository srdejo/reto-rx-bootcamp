package com.pragma.bootcamp.infrastructure.input.rest;

import com.pragma.bootcamp.application.dto.request.BootcampRequestDto;
import com.pragma.bootcamp.application.dto.response.BootcampResponseDto;
import com.pragma.bootcamp.application.dto.response.PagedResponseDto;
import com.pragma.bootcamp.application.handler.IBootcampHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bootcamps/")
@RequiredArgsConstructor
public class BootcampRestController {

    private final IBootcampHandler bootcampHandler;

    @Operation(summary = "Add a new bootcamp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bootcamp created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public Mono<ResponseEntity<Void>> saveBootcamp(@Valid @RequestBody BootcampRequestDto bootcampRequestDto) {
        return bootcampHandler.saveBootcamp(bootcampRequestDto)
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    }

    @Operation(summary = "Get a paginated list of bootcamps, sorted by name or capacity count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page of bootcamps returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagedResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sort parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "No data found", content = @Content)
    })
    @GetMapping()
    public Mono<PagedResponseDto<BootcampResponseDto>> getAllBootcamps(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", schema = @Schema(allowableValues = {"name", "capacityCount"}, defaultValue = "name"))
            @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction", schema = @Schema(allowableValues = {"asc", "desc"}, defaultValue = "asc"))
            @RequestParam(defaultValue = "asc") String direction) {
        return bootcampHandler.getAllBootcamps(page, size, sortBy, direction);
    }

    @Operation(summary = "Delete a bootcamp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bootcamp deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Bootcamp not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid bootcamp ID", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteBootcamp(
            @PathVariable Long id) {

        return bootcampHandler.deleteBootcamp(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Get a bootcamp by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bootcamp found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BootcampResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Bootcamp not found", content = @Content)
    })
    @GetMapping("{id}")
    public Mono<BootcampResponseDto> getBootcampById(@PathVariable Long id) {
        return bootcampHandler.getBootcampById(id);
    }
}
