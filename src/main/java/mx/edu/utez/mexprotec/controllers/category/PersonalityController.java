package mx.edu.utez.mexprotec.controllers.category;

import jakarta.validation.Valid;
import mx.edu.utez.mexprotec.dtos.category.PersonalityDto;
import mx.edu.utez.mexprotec.models.animals.personality.Personality;
import mx.edu.utez.mexprotec.services.category.PersonalityService;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personality/")
@CrossOrigin(origins = {"*"})
public class PersonalityController {

    private final PersonalityService personalityService;

    @Autowired
    private PersonalityController(PersonalityService personalityService) {
        this.personalityService = personalityService;
    }

    @GetMapping("/")
    public ResponseEntity<CustomResponse<List<Personality>>> getAll() {
        return new ResponseEntity<>(
                this.personalityService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Personality>> getOne(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(
                this.personalityService.getOne(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse<Personality>> insert(
            @RequestBody PersonalityDto dto, @Valid BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        CustomResponse<Personality> response = this.personalityService.insert(dto.getPersonality());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Personality>> update(
            @RequestBody PersonalityDto dto, @Valid BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                this.personalityService.update(dto.getPersonality()),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Boolean>> deleteById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(
                this.personalityService.deleteById(id),
                HttpStatus.OK
        );
    }
}
