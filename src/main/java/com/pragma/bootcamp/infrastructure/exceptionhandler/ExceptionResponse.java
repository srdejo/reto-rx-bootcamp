package com.pragma.bootcamp.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    INVALID_REQUEST("Invalid request body"),
    INVALID_NUMBER_OF_CAPACITIES_ASSOCIATED("Invalid number of capacities associated with the requested petition"),
    DUPLICATE_CAPACITY_ID("Duplicate capacity id"),
    CAPACITY_NOT_FOUND("One or more capacities do not exist"),
    CAPACITY_SERVICE_UNAVAILABLE("Capacity service is unavailable, please try again later"),
    INVALID_PAGINATION_PARAMETER("Invalid pagination or sort parameters");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

}
