package io.github.mrspock182.repository;

import io.github.mrspock182.entity.Person;

public interface PersonRepository {
    Person save(Person person);

    Person findById(String id);
}
