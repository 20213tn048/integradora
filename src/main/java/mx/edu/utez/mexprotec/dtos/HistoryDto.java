package mx.edu.utez.mexprotec.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.animals.Animals;
import mx.edu.utez.mexprotec.models.history.History;
import mx.edu.utez.mexprotec.models.processed.Processed;
import mx.edu.utez.mexprotec.models.users.Users;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HistoryDto {

    private UUID id;
    private Users cliente;
    private Animals animal;
    private Processed adoptionProcessed;
    private Boolean status;

    public History getHistory(){
        return new History(
                getId(),
                getCliente(),
                getAnimal(),
                getAdoptionProcessed(),
                getStatus()
        );
    }
}
