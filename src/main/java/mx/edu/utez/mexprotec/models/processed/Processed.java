package mx.edu.utez.mexprotec.models.processed;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.adoption.Adoption;
import mx.edu.utez.mexprotec.models.users.Users;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name="adoption_processed")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Processed {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_adoption")
    private Adoption adoption;

    @ManyToOne
    @JoinColumn(name = "id_users")
    private Users moderador;

    @Column(columnDefinition = "boolean default true")
    private Boolean status;
}
