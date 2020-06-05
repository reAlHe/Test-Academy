package ch.swissq.personservice.api.exposing;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import ch.swissq.personservice.model.PersonRepository;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static ch.swissq.personservice.builder.PersonBuilder.aPerson;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("person-service")
@PactBroker(host = "localhost", port = "9292")
@ActiveProfiles("test")
public class PersonControllerTest {

    @Autowired
    private PersonRepository personRepository;

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("there are person details provided")
    void insertProductDetails() {
        var person = aPerson()
                .withId(1)
                .withName("Silvio")
                .withCompany("UBS")
                .withTelephone("98765")
                .build();
        personRepository.save(person);
    }

}