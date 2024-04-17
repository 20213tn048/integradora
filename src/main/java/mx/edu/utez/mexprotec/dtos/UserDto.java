package mx.edu.utez.mexprotec.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import mx.edu.utez.mexprotec.models.rol.Rol;
import mx.edu.utez.mexprotec.models.users.Users;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {

    private UUID id;
    @NotNull
    private String nameUser;
    @NotNull
    private String name;
    @NotNull
    private String lastname;
    private String lastnameMatern;
    @Email(message = "Correo electrónico no válido")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@gmail\\.[a-zA-Z]{2,}$", message = "El correo electrónico debe ser de Gmail")
    private String email;
    private String phoneNumber;
    private String localitation;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "La contraseña no puede estar en blanco")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
    private Rol rol;
    private Boolean status;

    public Users getUsers(){
        return new Users(
                getId(),
                getNameUser(),
                getName(),
                getLastname(),
                getLastnameMatern(),
                getEmail(),
                getPhoneNumber(),
                getLocalitation(),
                getPassword(),
                getRol(),
                getStatus()
        );
    }
}
