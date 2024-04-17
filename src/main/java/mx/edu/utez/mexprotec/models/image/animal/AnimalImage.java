package mx.edu.utez.mexprotec.models.image.animal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.animals.Animals;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "animal_images")
@Getter
@Setter
public class AnimalImage {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "animals_id")
    @JsonBackReference
    private Animals animal;

    @Column(name = "image_url")
    private String imageUrl;
}
