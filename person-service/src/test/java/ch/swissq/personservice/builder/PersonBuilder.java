package ch.swissq.personservice.builder;

import ch.swissq.personservice.model.Person;

public final class PersonBuilder {
    private long id;
    private String name;
    private String company;
    private String telephone;

    private PersonBuilder() {
    }

    public static PersonBuilder aPerson() {
        return new PersonBuilder();
    }

    public PersonBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public PersonBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder withCompany(String company) {
        this.company = company;
        return this;
    }

    public PersonBuilder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public Person build() {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setCompany(company);
        person.setTelephone(telephone);
        return person;
    }
}
