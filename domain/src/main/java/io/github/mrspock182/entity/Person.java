package io.github.mrspock182.entity;

import io.github.mrspock182.entity.enumerable.PillEnum;

import java.time.LocalDate;

public record Person(
        String id,
        String name,
        PillEnum pill,
        String message,
        Boolean isChosen,
        LocalDate createIn
) {
}
