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
            // TODO 13) fix this data
            o.numberType("xx", 1);
            o.stringType("name", "yy");

            o.stringType("telephone", "12345");
        };
    }

}
