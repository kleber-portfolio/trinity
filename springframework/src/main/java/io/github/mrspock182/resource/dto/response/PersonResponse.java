package io.github.mrspock182.resource.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.mrspock182.entity.enumerable.PillEnum;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PersonResponse(
        String id,
        String name,
        PillEnum pill,
        String message,
        Boolean isChosen,
        LocalDate createIn
) {
}