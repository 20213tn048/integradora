package mx.edu.utez.mexprotec.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.animals.Animals;
import mx.edu.utez.mexprotec.models.animals.ApprovalStatus;
import mx.edu.utez.mexprotec.models.animals.personality.Personality;
import mx.edu.utez.mexprotec.models.animals.race.Race;
import mx.edu.utez.mexprotec.models.animals.typePet.TypePet;
import mx.edu.utez.mexprotec.models.image.animal.AnimalImage;
import mx.edu.utez.mexprotec.models.users.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AnimalDto {

    private UUID id;
    private String namePet;
    private String location;
    private TypePet typePet;
    private Race race;
    private Personality personality;
    private String sex;
    private String size;
    private Double weight;
    private Integer age;
    private String color;
    private Boolean sterilized;
    private String description;
    private List<String> imageUrl;
    private ApprovalStatus approvalStatus;
    private String moderatorComment;
    private Users register;

    public Animals toAnimals() {
        Animals animal = new Animals();
        animal.setId(getId());
        animal.setNamePet(getNamePet());
        animal.setLocation(getLocation());
        animal.setTypePet(getTypePet());
        animal.setRace(getRace());
        animal.setPersonality(getPersonality());
        animal.setSex(getSex());
        animal.setSize(getSize());
        animal.setWeight(getWeight());
        animal.setAge(getAge());
        animal.setColor(getColor());
        animal.setSterilized(getSterilized());
        animal.setDescription(getDescription());

        List<AnimalImage> animalImages = new ArrayList<>();
        if (getImageUrl() != null) {
            for (String imageUrl : getImageUrl()) {
                AnimalImage animalImage = new AnimalImage();
                animalImage.setAnimal(animal);
                animalImage.setImageUrl(imageUrl);
                animalImages.add(animalImage);
            }
        }
        animal.setImages(animalImages);
        animal.setApprovalStatus(approvalStatus);
        animal.setModeratorComment(getModeratorComment());
        animal.setRegister(getRegister());
        return animal;
    }
}
