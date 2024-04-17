package mx.edu.utez.mexprotec.services.imageCloudy;

import mx.edu.utez.mexprotec.config.service.CloudinaryService;
import mx.edu.utez.mexprotec.models.adoption.Adoption;
import mx.edu.utez.mexprotec.models.image.adoption.AdoptionImage;
import mx.edu.utez.mexprotec.models.image.adoption.AdoptionImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdoptionImageService {

    private final CloudinaryService cloudinaryService;
    private final AdoptionImageRepository adoptionImageRepository;

    @Autowired
    private AdoptionImageService(CloudinaryService cloudinaryService, AdoptionImageRepository adoptionImageRepository){
        this.cloudinaryService = cloudinaryService;
        this.adoptionImageRepository = adoptionImageRepository;
    }

    public AdoptionImage uploadImage(MultipartFile file, Adoption adoption) {
        try {
            String imageUrl = cloudinaryService.uploadFile(file, "adoption_images");
            AdoptionImage adoptionImage = new AdoptionImage();
            adoptionImage.setAdoption(adoption);
            adoptionImage.setImageUrl(imageUrl);
            return adoptionImageRepository.save(adoptionImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
