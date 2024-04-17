package mx.edu.utez.mexprotec.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.mexprotec.models.adoption.Adoption;
import mx.edu.utez.mexprotec.models.animals.Animals;
import mx.edu.utez.mexprotec.models.animals.ApprovalStatus;
import mx.edu.utez.mexprotec.models.image.adoption.AdoptionImage;
import mx.edu.utez.mexprotec.models.users.Users;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AdoptionDto {

    private UUID id;
    private Animals animal;
    private Users adopter;
    private String description;
    private List<String> imageUrl;
    private LocalDateTime creationDate;
    private ApprovalStatus approvalStatus;

    private String moderatorComment;


    public Adoption toAdoption() {
        Adoption adoption = new Adoption();
        adoption.setId(id);
        adoption.setAnimal(animal);
        adoption.setAdopter(adopter);
        adoption.setDescription(description);

        List<AdoptionImage> adoptionImages = new ArrayList<>();
        if (imageUrl != null) {
            for (String url : imageUrl) {
                AdoptionImage adoptionImage = new AdoptionImage();
                adoptionImage.setAdoption(adoption);
                adoptionImage.setImageUrl(url);
                adoptionImages.add(adoptionImage);
            }
        }
        adoption.setImages(adoptionImages);
        adoption.setCreationDate(creationDate);
        adoption.setApprovalStatus(approvalStatus);
        adoption.setModeratorComment(moderatorComment);
        return adoption;
    }

}
