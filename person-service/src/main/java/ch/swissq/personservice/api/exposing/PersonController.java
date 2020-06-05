package ch.swissq.personservice.api.exposing;

import ch.swissq.personservice.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping("/register")
    public ResponseEntity<PersonResponse> registerPerson(@RequestBody PersonRequest personRequest) {
        var registeredPerson = personService.registerPerson(personRequest.asPerson());
        return new ResponseEntity<>(PersonResponse.fromPerson(registeredPerson), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> updatePerson(@PathVariable long id, @RequestBody PersonRequest personRequest) {
        var updatedPerson = personService.updatePerson(id, personRequest.asPerson());
        return ResponseEntity.ok().body(PersonResponse.fromPerson(updatedPerson));
    }

    @GetMapping
    public ResponseEntity<Set<PersonResponse>> getPersonDetails(@RequestParam("ids") List<Long> ids) {
        var foundPerson = personService.findPersonsWithIds((new HashSet<>(ids)));
        var personResponses = foundPerson.stream().map(PersonResponse::fromPerson).collect(Collectors.toSet());
        return ResponseEntity.ok(personResponses);
    }

    @RequestMapping(method = RequestMethod.HEAD, value = "/{id}")
    public ResponseEntity<Void> existsPersonWithId(@PathVariable long id) {
        boolean isPersonExisting = personService.existsPersonWithId(id);
        if (!isPersonExisting) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
