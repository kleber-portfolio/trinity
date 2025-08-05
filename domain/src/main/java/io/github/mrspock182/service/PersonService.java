package io.github.mrspock182.service;


import io.github.mrspock182.entity.Person;
import io.github.mrspock182.messaging.PersonProducer;
import io.github.mrspock182.repository.PersonRepository;

import java.util.Optional;

public class PersonService {
    private final PersonProducer producer;
    private final PersonRepository repository;

    public PersonService(
            PersonProducer producer,
            PersonRepository repository) {
        this.producer = producer;
        this.repository = repository;
    }

    public Person format(final Person person) {
        return Optional.ofNullable(person)
                .map(this::nameToUppercase)
                .map(repository::save)
                .map(producer::produce)
                .orElseThrow(() -> new RuntimeException("Error in person process"));
    }

    private Person nameToUppercase(final Person person) {
        return new Person(
                person.id(),
                person.name().toUpperCase(),
                person.pill(),
                person.message(),
                person.isChosen(),
                person.createIn());
    }
}