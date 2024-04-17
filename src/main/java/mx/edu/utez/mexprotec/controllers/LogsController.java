package mx.edu.utez.mexprotec.controllers;

import mx.edu.utez.mexprotec.models.logs.Logs;
import mx.edu.utez.mexprotec.services.LogsService;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/logs/")
@CrossOrigin(origins = "http://localhost:5173/")
public class LogsController {

    private final LogsService service;
    
    @Autowired
    private LogsController(LogsService service) {
        this.service = service;
    }

    @GetMapping("/")
    public CustomResponse<List<Logs>> getAll() throws SQLException {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CustomResponse<Logs> getById(
            @PathVariable String id) throws SQLException {
        return service.getById(id);
    }

    @PostMapping
    public CustomResponse<Logs> save(
            @RequestBody Logs logs) throws SQLException {
        return service.save(logs);
    }
}
