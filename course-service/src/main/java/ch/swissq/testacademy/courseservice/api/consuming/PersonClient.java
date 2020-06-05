package ch.swissq.testacademy.courseservice.api.consuming;

import ch.swissq.testacademy.courseservice.model.Person;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "personClient", url = "${feign.url}")
public interface PersonClient {

    @RequestMapping(method=RequestMethod.HEAD, value = "/person/{personId}")
    ResponseEntity<Void> retrievePersonIdExists(@PathVariable("personId") Long personId);

    @GetMapping(value = "/person")
    ResponseEntity<List<Person>> retrievePersonInformationForIds(@RequestParam("ids") Set<Long> ids);
}
