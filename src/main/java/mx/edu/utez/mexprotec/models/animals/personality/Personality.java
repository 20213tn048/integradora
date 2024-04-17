package mx.edu.utez.mexprotec.models.animals.personality;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.animals.Animals;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name= "personality")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Personality {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "personality_pet", nullable = false)
    private String personalityPet;

    @OneToMany(mappedBy = "personality")
    @JsonIgnore
    private List<Animals> animals;

}
