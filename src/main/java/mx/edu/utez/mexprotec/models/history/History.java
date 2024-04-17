package mx.edu.utez.mexprotec.models.history;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.animals.Animals;
import mx.edu.utez.mexprotec.models.processed.Processed;
import mx.edu.utez.mexprotec.models.users.Users;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name="historial")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class History {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_users")
    private Users cliente;

    @ManyToOne
    @JoinColumn(name = "id_animals")
    private Animals animal;

    @ManyToOne
    @JoinColumn(name = "id_adoption_processed")
    private Processed adoptionProcessed;

    @Column(name = "status")
    private Boolean status;

}
