package io.github.mrspock182.messaging.adapter;

import io.github.mrspock182.entity.Person;
import io.github.mrspock182.messaging.dto.PersonProducerDto;

public class PersonProducerAdapter {
    private PersonProducerAdapter() {
    }

    public static Person cast(final PersonProducerDto producer) {
        return new Person(
                producer.id(),
                producer.name(),
                null,
                null,
                null,
                producer.createIn());
    }

    public static PersonProducerDto cast(final Person producer) {
        return new PersonProducerDto(
                producer.id(),
                producer.name(),
                producer.createIn());
    }
}