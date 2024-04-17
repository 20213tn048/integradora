package mx.edu.utez.mexprotec.controllers;

import jakarta.validation.Valid;
import mx.edu.utez.mexprotec.dtos.AdoptionDto;

import mx.edu.utez.mexprotec.models.adoption.Adoption;
import mx.edu.utez.mexprotec.models.animals.ApprovalStatus;
import mx.edu.utez.mexprotec.services.AdoptionService;
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
@RequestMapping("/api/adoption/")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class AdoptionController {
    
    private final AdoptionService adoptionService;
    
    @Autowired
    private AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @GetMapping("/")
    public ResponseEntity<CustomResponse<List<Adoption>>> getAll() {
        return new ResponseEntity<>(
                this.adoptionService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Adoption>> getOne(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(
                this.adoptionService.getOne(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/pending-approval")
    public ResponseEntity<CustomResponse<List<Adoption>>> getPendingApproval() {
        CustomResponse<List<Adoption>> response = adoptionService.getPendingApprovalAdoption();
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    @GetMapping("/approved")
    public ResponseEntity<CustomResponse<List<Adoption>>> getApproved() {
        CustomResponse<List<Adoption>> response = adoptionService.getApprovedAdoption();
        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse<Adoption>> insert(
            @ModelAttribute AdoptionDto dto,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles,
            @Valid BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                this.adoptionService.insert(dto.toAdoption(), imageFiles),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Adoption>> update( @PathVariable UUID id,
                                                            @ModelAttribute AdoptionDto adoptionDto,
                                                            @RequestParam(required = false) List<MultipartFile> imageFiles){
        CustomResponse<Adoption> response = adoptionService.update(id, adoptionDto, imageFiles);
        if (response.isError()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/approval")
    public ResponseEntity<CustomResponse<String>> approvedOrRejectAdoption(@PathVariable UUID id,
                                                                           @RequestParam ApprovalStatus approvalStatus,
                                                                           @RequestParam String moderatorComment) {
        CustomResponse<Boolean> response = adoptionService.approveOrRejectAdoption(id, approvalStatus, moderatorComment);
        String message;
        HttpStatus httpStatus;

        if (response.getData() != null && response.getData()) {
            message = "Adopción cambiada correctamente";
            httpStatus = HttpStatus.OK;
        } else {
            if (ApprovalStatus.REJECTED.equals(approvalStatus)) {
                message = "Adopción rechazada correctamente";
            } else {
                message = "No se pudo procesar la operación de adopción";
            }
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(new CustomResponse<>(message, false, httpStatus.value(), message), httpStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Boolean>> delete(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(
                this.adoptionService.delete(id),
                HttpStatus.OK
        );
    }
}
