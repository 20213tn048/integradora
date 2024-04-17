package mx.edu.utez.mexprotec.controllers;

import jakarta.validation.Valid;
import mx.edu.utez.mexprotec.dtos.UserDto;
import mx.edu.utez.mexprotec.models.users.Users;
import mx.edu.utez.mexprotec.services.UserService;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class UserController {

    private final UserService userService;
    
    @Autowired
    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<CustomResponse<List<Users>>> getAll(){
        return new ResponseEntity<>(
                this.userService.getAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Users>> getById(
            @PathVariable("id") UUID id
    ){
        return new ResponseEntity<>(
                this.userService.getOne(id),
                HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<CustomResponse<List<Users>>> getActive() {
        return new ResponseEntity<>(
                this.userService.getActive(),
                HttpStatus.OK
        );
    }

    @GetMapping("/inactive")
    public ResponseEntity<CustomResponse<List<Users>>> getInactive() {
        return new ResponseEntity<>(
                this.userService.getInactive(),
                HttpStatus.OK
        );
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<CustomResponse<List<Users>>> getByRole(@PathVariable("roleName") String roleName) {
        return new ResponseEntity<>(
                this.userService.getByRole(roleName),
                HttpStatus.OK
        );
    }

    @GetMapping("/roles/admin-moderador")
    public ResponseEntity<CustomResponse<List<Users>>> getUsersByAdminAndModeratorRoles() {
        return new ResponseEntity<>(
                this.userService.getUsersByAdminAndModeratorRoles(),
                HttpStatus.OK
        );
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse<Users>> insert(@RequestBody UserDto usuario) {
        try {
            CustomResponse<Users> response = this.userService.insert(usuario.getUsers());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Error al enviar correo: " + e.getMessage();
            return new ResponseEntity<>(
                    new CustomResponse<>(null, true, 400, errorMessage),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Users>> update(
            @RequestBody UserDto dto, @Valid BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                this.userService.update(dto.getUsers()),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomResponse<Boolean>> changeUserStatus(
            @PathVariable("id") UUID id,
            @RequestBody Map<String, Object> payload
    ) {
        try {
            boolean newStatus = (boolean) payload.get("status");
            CustomResponse<Boolean> response = this.userService.changeUserStatus(id, newStatus);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new CustomResponse<>(null, true, 500, "Error al cambiar estado de usuario"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
