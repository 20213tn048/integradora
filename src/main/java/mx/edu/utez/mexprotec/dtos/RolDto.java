package mx.edu.utez.mexprotec.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.rol.Rol;
import mx.edu.utez.mexprotec.models.users.Users;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RolDto {

    private UUID idRol;
    private String nrol;
    private Boolean status;
    private Set<Users> usuario;

    public Rol getRol(){
        return new Rol(
                getIdRol(),
                getNrol(),
                getStatus(),
                getUsuario()
        );
    }
}
