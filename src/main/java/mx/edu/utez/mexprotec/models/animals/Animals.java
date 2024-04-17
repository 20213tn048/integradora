package mx.edu.utez.mexprotec.models.animals;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.animals.personality.Personality;
import mx.edu.utez.mexprotec.models.animals.race.Race;
import mx.edu.utez.mexprotec.models.animals.typePet.TypePet;
import mx.edu.utez.mexprotec.models.image.animal.AnimalImage;
import mx.edu.utez.mexprotec.models.users.Users;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name="animals")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Animals {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name_pet", nullable = false)
    private String namePet;

    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "type_pet", referencedColumnName = "id")
    private TypePet typePet;

    @ManyToOne
    @JoinColumn(name = "race", referencedColumnName = "id")
    private Race race;

    @ManyToOne
    @JoinColumn(name = "personality", referencedColumnName = "id")
    private Personality personality;

    @Column(name = "sex", nullable = false)
    private String sex;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "sterilized")
    private Boolean sterilized;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AnimalImage> images;

    @Column(name = "approval_status", nullable = false)
    private ApprovalStatus approvalStatus;

    @Column(name = "moderator_comment")
    private String moderatorComment;

    @ManyToOne
    @JoinColumn(name = "id_register")
    private Users register;

}
