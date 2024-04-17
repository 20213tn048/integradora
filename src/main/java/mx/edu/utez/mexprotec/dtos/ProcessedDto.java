package mx.edu.utez.mexprotec.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.adoption.Adoption;
import mx.edu.utez.mexprotec.models.processed.Processed;
import mx.edu.utez.mexprotec.models.users.Users;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProcessedDto {

    private UUID id;
    private Adoption adoption;
    private Users moderador;
    private Boolean status;

    public Processed getProcessed(){
        return new Processed(
                getId(),
                getAdoption(),
                getModerador(),
                getStatus()
        );
    }
}
