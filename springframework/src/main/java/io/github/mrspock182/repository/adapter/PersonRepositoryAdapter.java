package io.github.mrspock182.repository.adapter;

import io.github.mrspock182.entity.Person;
import io.github.mrspock182.repository.dto.PersonOrmDynamoDB;

public class PersonRepositoryAdapter {
    private PersonRepositoryAdapter() {
    }

    public static PersonOrmDynamoDB cast(final Person person) {
        PersonOrmDynamoDB orm = new PersonOrmDynamoDB();
        orm.setId(person.id());
        orm.setName(person.name());
        orm.setCreateIn(person.createIn());
        return orm;
    }

    public static Person cast(final PersonOrmDynamoDB orm) {
        return new Person(
                orm.getId(),
                orm.getName(),
                orm.getPill(),
                orm.getMessage(),
                orm.getChosen(),
                orm.getCreateIn());
    }
}