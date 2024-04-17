package mx.edu.utez.mexprotec.models.rol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.users.Users;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="rol")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rol {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID idRol;

    @Column(name = "nrol", nullable = false)
    private String nrol;

    @Column(nullable = false, columnDefinition = "boolean default true")
    @JsonIgnore
    private Boolean status;

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private Set<Users> usuario;
}
