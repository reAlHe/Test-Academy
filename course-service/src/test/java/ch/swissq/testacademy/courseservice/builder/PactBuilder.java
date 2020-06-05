package ch.swissq.testacademy.courseservice.builder;

import au.com.dius.pact.consumer.dsl.DslPart;
import io.pactfoundation.consumer.dsl.LambdaDslObject;

import java.util.function.Consumer;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonArray;

public class PactBuilder {

    public static DslPart participants() {
        return newJsonArray(o -> {
            o.object(person());
        }).build();
    }

    private static Consumer<LambdaDslObject> person() {
        return o -> {
            o.numberType("id", 1);
            o.stringType("name", "Silvio");
            o.stringType("company", "SwissQ");
            o.stringType("telephone", "12345");
        };
    }

}
