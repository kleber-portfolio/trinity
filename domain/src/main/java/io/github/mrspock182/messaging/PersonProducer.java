package io.github.mrspock182.messaging;

import io.github.mrspock182.entity.Person;

public interface PersonProducer {
    Person produce(Person person);
}
