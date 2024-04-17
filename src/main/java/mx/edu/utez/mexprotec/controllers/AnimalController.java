package mx.edu.utez.mexprotec.controllers;

import jakarta.validation.Valid;
import mx.edu.utez.mexprotec.dtos.AnimalDto;
import mx.edu.utez.mexprotec.models.animals.Animals;
import mx.edu.utez.mexprotec.models.animals.ApprovalStatus;
import mx.edu.utez.mexprotec.models.animals.personality.Personality;
import mx.edu.utez.mexprotec.models.animals.race.Race;
import mx.edu.utez.mexprotec.models.animals.typePet.TypePet;
import mx.edu.utez.mexprotec.services.AnimalService;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/animals/")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class AnimalController {

    private final AnimalService animalService;
    
    @Autowired
    private AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("/")
    public ResponseEntity<CustomResponse<List<Animals>>> getAll() {
        return new ResponseEntity<>(
                this.animalService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Animals>> getOne(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(
                this.animalService.getOne(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/type/{typePetId}")
    public ResponseEntity<CustomResponse<List<Animals>>> getAnimalsByTypePet(@PathVariable UUID typePetId) {
        TypePet typePet = new TypePet();
        typePet.setId(typePetId);

        CustomResponse<List<Animals>> response = this.animalService.getAnimalsByTypePet(typePet);
        if (response.getError()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/race/{raceId}")
    public ResponseEntity<CustomResponse<List<Animals>>> getAnimalsByRace(@PathVariable UUID raceId) {
        Race race = new Race();
        race.setId(raceId);

        CustomResponse<List<Animals>> response = this.animalService.getAnimalsByRace(race);
        if (response.getError()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/personality/{personalityId}")
    public ResponseEntity<CustomResponse<List<Animals>>> getAnimalsByPersonality(@PathVariable UUID personalityId) {
        Personality personality = new Personality();
        personality.setId(personalityId);

        CustomResponse<List<Animals>> response = this.animalService.getAnimalsByPersonality(personality);
        if (response.getError()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/female")
    public ResponseEntity<CustomResponse<List<Animals>>> getFemaleAnimals() {
        CustomResponse<List<Animals>> response = this.animalService.getFemaleAnimals();
        if (response.getError()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/male")
    public ResponseEntity<CustomResponse<List<Animals>>> getMaleAnimals() {
        CustomResponse<List<Animals>> response = this.animalService.getMaleAnimals();
        if (response.getError()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/pending-approval")
    public ResponseEntity<CustomResponse<List<Animals>>> getPendingApprovalAnimals() {
        CustomResponse<List<Animals>> response = animalService.getPendingApprovalAnimals();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/approved")
    public ResponseEntity<CustomResponse<List<Animals>>> getApprovedAnimals() {
        CustomResponse<List<Animals>> response = animalService.getApprovedAnimals();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse<Animals>> insert(
            @ModelAttribute AnimalDto dto,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles,
            @Valid BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                this.animalService.insert(dto.toAnimals(), imageFiles),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Animals>> updateAnimal(@PathVariable UUID id,
                                                                @ModelAttribute AnimalDto animalDto,
                                                                @RequestParam(required = false) List<MultipartFile> imageFiles) {
        CustomResponse<Animals> response = animalService.update(id, animalDto, imageFiles);
        if (response.isError()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/approval")
    public ResponseEntity<CustomResponse<String>> approveOrRejectAnimal(@PathVariable UUID id,
                                                                        @RequestParam ApprovalStatus approvalStatus,
                                                                        @RequestParam String moderatorComment) {
        CustomResponse<Boolean> response = animalService.approveOrRejectAnimal(id, approvalStatus, moderatorComment);
        String message;
        HttpStatus httpStatus;

        if (response.getData() != null && response.getData()) {
            message = "Estado cambiado correctamente";
            httpStatus = HttpStatus.OK;
        } else {
            message = "Animal no encontrado";
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(new CustomResponse<>(message, false, httpStatus.value(), message), httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Boolean>> delete(
            @PathVariable("id") UUID id) {
        return new ResponseEntity<>(
                this.animalService.delete(id),
                HttpStatus.OK
        );
    }
}
