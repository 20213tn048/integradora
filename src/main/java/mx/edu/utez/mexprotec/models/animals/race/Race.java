package mx.edu.utez.mexprotec.models.animals.race;

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
@Table(name= "race")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Race {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "race_pet", nullable = false)
    private String racePet;

    @OneToMany(mappedBy = "race")
    @JsonIgnore
    private List<Animals> animals;

}
