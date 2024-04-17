package mx.edu.utez.mexprotec.controllers;

import jakarta.validation.Valid;
import mx.edu.utez.mexprotec.dtos.HistoryDto;
import mx.edu.utez.mexprotec.models.history.History;
import mx.edu.utez.mexprotec.services.HistoryService;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/historial/")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class HistoryController {
        
        private final HistoryService historyService;
        
        @Autowired
        private HistoryController(HistoryService historyService) {
                this.historyService = historyService;
        }

    @GetMapping("/")
    public ResponseEntity<CustomResponse<List<History>>> getAll() {
        return new ResponseEntity<>(
                this.historyService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/getActive")
    public ResponseEntity<CustomResponse<List<History>>>
    getAllActive(){
        return new ResponseEntity<>(
                this.historyService.getAllActive(),
                HttpStatus.OK
        );
    }

    @GetMapping("/getAllInactive")
    public ResponseEntity<CustomResponse<List<History>>>
    getAllInactive(){
        return new ResponseEntity<>(
                this.historyService.getAllInactive(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<History>> getOne(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(
                this.historyService.getOne(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse<History>> insert(
            @RequestBody HistoryDto dto, @Valid BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        CustomResponse<History> response = this.historyService.insert(dto.getHistory());
        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<History>> update(
            @RequestBody HistoryDto dto, @Valid BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                this.historyService.update(dto.getHistory()),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomResponse<Boolean>> enableOrDisable(
            @RequestBody HistoryDto dto) {
        return new ResponseEntity<>(
                this.historyService.changeStatus(dto.getHistory()),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Boolean>> delete(
            @PathVariable("id") UUID id) {
        return new ResponseEntity<>(
                this.historyService.delete(id),
                HttpStatus.OK
        );
    }

}
