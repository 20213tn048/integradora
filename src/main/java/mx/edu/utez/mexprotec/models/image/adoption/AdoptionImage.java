package mx.edu.utez.mexprotec.models.image.adoption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.adoption.Adoption;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "adoption_images")
@Getter
@Setter
public class AdoptionImage {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "adoption_id")
    @JsonBackReference
    private Adoption adoption;

    @Column(name = "image_url")
    private String imageUrl;

}
