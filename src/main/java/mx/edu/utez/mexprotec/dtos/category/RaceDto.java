package mx.edu.utez.mexprotec.dtos.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.animals.Animals;
import mx.edu.utez.mexprotec.models.animals.race.Race;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RaceDto {

    private UUID id;
    private String racePet;
    @JsonIgnore
    private List<Animals> animals;

    public Race getRace(){
        return new Race(
                getId(),
                getRacePet(),
                getAnimals()
        );
    }
}
