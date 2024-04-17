package mx.edu.utez.mexprotec.services;

import mx.edu.utez.mexprotec.config.service.CloudinaryService;
import mx.edu.utez.mexprotec.dtos.AdoptionDto;
import mx.edu.utez.mexprotec.models.adoption.Adoption;
import mx.edu.utez.mexprotec.models.adoption.AdoptionRepository;
import mx.edu.utez.mexprotec.models.animals.ApprovalStatus;
import mx.edu.utez.mexprotec.models.image.adoption.AdoptionImage;
import mx.edu.utez.mexprotec.models.image.adoption.AdoptionImageRepository;
import mx.edu.utez.mexprotec.services.imageCloudy.AdoptionLimitService;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final CloudinaryService cloudinaryService;
    private final AdoptionImageRepository adoptionImageRepository;
    private final AdoptionLimitService adoptionLimitService;
    
    @Autowired
    private AdoptionService(AdoptionRepository adoptionRepository, CloudinaryService cloudinaryService, AdoptionImageRepository adoptionImageRepository, AdoptionLimitService adoptionLimitService){
        this.adoptionRepository = adoptionRepository;
        this.cloudinaryService = cloudinaryService;
        this.adoptionImageRepository = adoptionImageRepository;
        this.adoptionLimitService = adoptionLimitService;
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Adoption>> getAll(){
        return new CustomResponse<>(
                this.adoptionRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }


    @Transactional(readOnly = true)
    public CustomResponse<Adoption> getOne(UUID id){
        Optional<Adoption> optional = this.adoptionRepository.findById(id);
        if (optional.isPresent()){
            return new CustomResponse<>(
                    optional.get(),
                    false,
                    200,
                    "Ok"
            );
        }else {
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "No encontrado"
            );
        }
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Adoption>> getPendingApprovalAdoption(){
        List<Adoption> pendingAdoptions = adoptionRepository.findByApprovalStatus(ApprovalStatus.PENDING);
        return new CustomResponse<>(pendingAdoptions, false, 200, "Adopciones pendientes de aprobación");
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Adoption>> getApprovedAdoption(){
        List<Adoption> approvedAdoptions = adoptionRepository.findByApprovalStatus(ApprovalStatus.APPROVED);
        return new CustomResponse<>(approvedAdoptions,
                false, 200, "Adopciones aprobadas");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Adoption> insert(Adoption adoption, List<MultipartFile> imageFiles) {
       try{
           if (adoptionLimitService.isAdoptionLimitReached()) {
               return new CustomResponse<>(null, true, 400, "Se ha alcanzado el límite de adopciones por día");
           }
           Adoption savedAdoption = adoptionRepository.saveAndFlush(adoption);
           List<AdoptionImage> images = new ArrayList<>();
           for (MultipartFile file : imageFiles) {
               String imageUrl = cloudinaryService.uploadFile(file, "adoption_images");
               AdoptionImage adoptionImage = new AdoptionImage();
               adoptionImage.setAdoption(savedAdoption);
               adoptionImage.setImageUrl(imageUrl);
               images.add(adoptionImage);
           }
           savedAdoption.setImages(images);
           adoptionImageRepository.saveAll(images);
           return new CustomResponse<>(savedAdoption, false, 200, "Adopción registrada correctamente");
       }catch (Exception e) {
           e.printStackTrace();
           return new CustomResponse<> (null, true, 400, "Error al registrar la adopción");
       }

    }

    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Adoption> update(UUID id, AdoptionDto adoptionDto, List<MultipartFile> imageFiles){
        try {
            Adoption adoption = adoptionRepository.findById(adoptionDto.getId()).orElse(null);
            if (adoption == null) {
                return new CustomResponse<>(null, true, 404, "No se encontró la adopción con ID " + adoptionDto.getId());
            }
            adoption.setAnimal(adoptionDto.getAnimal());
            adoption.setAdopter(adoptionDto.getAdopter());
            adoption.setDescription(adoptionDto.getDescription());
            adoption.setCreationDate(adoptionDto.getCreationDate());
            adoption.setApprovalStatus(adoptionDto.getApprovalStatus());

            Adoption savedAdoption = adoptionRepository.save(adoption);

            if (imageFiles != null && !imageFiles.isEmpty()) {
                List<AdoptionImage> images = new ArrayList<>();
                for (MultipartFile file : imageFiles) {
                    String imageUrl = cloudinaryService.uploadFile(file, "adoption_images");
                    AdoptionImage adoptionImage = new AdoptionImage();
                    adoptionImage.setAdoption(savedAdoption);
                    adoptionImage.setImageUrl(imageUrl);
                    images.add(adoptionImage);
                }
                savedAdoption.setImages(images);
                adoptionImageRepository.saveAll(images);
            }
            return new CustomResponse<>(savedAdoption, false, 200, "Adopción actualizada correctamente");
        }catch (Exception e){
            e.printStackTrace();
            return new CustomResponse<>(null, true, 400, "Error al actualizar"
            );
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Boolean> approveOrRejectAdoption(UUID id, ApprovalStatus approvalStatus, String moderatorComment){
        Optional<Adoption> optional = this.adoptionRepository.findById(id);
        if(optional.isPresent()){
            Adoption adoption = optional.get();
            adoption.setApprovalStatus(approvalStatus);
            adoption.setModeratorComment(moderatorComment);
            this.adoptionRepository.save(adoption);
            return new CustomResponse<>(
                    true,
                    false,
                    200,
                    "¡Actualizado correctamente!"
            );
        }else {
            return new CustomResponse<>(
                    false,
                    true,
                    400,
                    "No encontrado"
            );
        }
    }

    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Boolean> delete(UUID id){
        if(!this.adoptionRepository.existsById(id)){
            return new CustomResponse<>(
                    false,
                    true,
                    400,
                    "No encontrado"
            );
        }
        this.adoptionRepository.deleteById(id);
        return new CustomResponse<>(
                true,
                false,
                200,
                "¡Eliminado correctamente!"
        );
    }
}
