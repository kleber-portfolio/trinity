package io.github.mrspock182.resource.adapter;

import io.github.mrspock182.entity.Person;
import io.github.mrspock182.resource.dto.request.PersonRequest;
import io.github.mrspock182.resource.dto.response.PersonResponse;

import java.time.LocalDate;
import java.util.UUID;

public class PersonResourceAdapter {
    private PersonResourceAdapter() {
    }

    public static Person cast(final PersonRequest person) {
        return new Person(
                UUID.randomUUID().toString(),
                person.name(),
                null,
                null,
                null,
                LocalDate.now());
    }

    public static PersonResponse cast(final Person person) {
        return new PersonResponse(
                person.id(),
                person.name(),
                person.pill(),
                person.message(),
                person.isChosen(),
                person.createIn());
    }
}