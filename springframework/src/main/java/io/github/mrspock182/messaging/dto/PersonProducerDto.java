package io.github.mrspock182.messaging.dto;

import java.time.LocalDate;

public record PersonProducerDto(
        String id,
        String name,
        LocalDate createIn
) {
}
