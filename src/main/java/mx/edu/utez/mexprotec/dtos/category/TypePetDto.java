package mx.edu.utez.mexprotec.dtos.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.animals.Animals;
import mx.edu.utez.mexprotec.models.animals.typePet.TypePet;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypePetDto {

    private UUID id;
    private String type;
    @JsonIgnore
    private List<Animals> animals;

    public TypePet getTypePet(){
        return new TypePet(
                getId(),
                getType(),
                getAnimals()
        );
    }
}
