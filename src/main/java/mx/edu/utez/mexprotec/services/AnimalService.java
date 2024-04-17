package mx.edu.utez.mexprotec.services;

import jakarta.mail.MessagingException;
import mx.edu.utez.mexprotec.config.service.CloudinaryService;
import mx.edu.utez.mexprotec.dtos.AnimalDto;
import mx.edu.utez.mexprotec.models.animals.Animals;
import mx.edu.utez.mexprotec.models.animals.AnimalsRepository;
import mx.edu.utez.mexprotec.models.animals.ApprovalStatus;
import mx.edu.utez.mexprotec.models.animals.personality.Personality;
import mx.edu.utez.mexprotec.models.animals.race.Race;
import mx.edu.utez.mexprotec.models.animals.typePet.TypePet;
import mx.edu.utez.mexprotec.models.image.animal.AnimalImage;
import mx.edu.utez.mexprotec.models.image.animal.AnimalImageRepository;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import mx.edu.utez.mexprotec.utils.Mailer;
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
public class AnimalService {

    private final AnimalsRepository animalsRepository;
    private final CloudinaryService cloudinaryService;
    private final AnimalImageRepository animalImageRepository;
    private final Mailer mailer;
    
    @Autowired
    private AnimalService(AnimalsRepository animalsRepository, CloudinaryService cloudinaryService, AnimalImageRepository animalImageRepository, Mailer mailer) {
        this.animalsRepository = animalsRepository;
        this.cloudinaryService = cloudinaryService;
        this.animalImageRepository = animalImageRepository;
        this.mailer =mailer;
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Animals>> getAll(){
        return new CustomResponse<>(
                this.animalsRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<Animals> getOne(UUID id){
        Optional<Animals> optional = this.animalsRepository.findById(id);
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
    public CustomResponse<List<Animals>> getAnimalsByTypePet(TypePet typePet) {
        List<Animals> animals = this.animalsRepository.findByTypePet(typePet);
        if (animals.isEmpty()) {
            return new CustomResponse<>(
                    null,
                    true,
                    404,
                    "No se encontraron animales para el tipo de mascota especificado"
            );
        }
        return new CustomResponse<>(
                animals,
                false,
                200,
                "Animales encontrados por"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Animals>> getAnimalsByRace(Race race) {
        List<Animals> animals = this.animalsRepository.findByRace(race);
        if (animals.isEmpty()) {
            return new CustomResponse<>(
                    null,
                    true,
                    404,
                    "No se encontraron animales para la raza especificada"
            );
        }
        return new CustomResponse<>(
                animals,
                false,
                200,
                "Animales encontrados por"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Animals>> getAnimalsByPersonality(Personality personality) {
        List<Animals> animals = this.animalsRepository.findByPersonality(personality);
        if (animals.isEmpty()) {
            return new CustomResponse<>(
                    null,
                    true,
                    404,
                    "No se encontraron animales para la personalidad especificada"
            );
        }
        return new CustomResponse<>(
                animals,
                false,
                200,
                "Animales encontrados por personalidad"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Animals>> getFemaleAnimals() {
        List<Animals> animals = this.animalsRepository.findBySex("Female");
        if (animals.isEmpty()) {
            return new CustomResponse<>(
                    null,
                    true,
                    404,
                    "No se encontraron animales hembra"
            );
        }
        return new CustomResponse<>(
                animals,
                false,
                200,
                "Animales hembra encontrados"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Animals>> getMaleAnimals() {
        List<Animals> animals = this.animalsRepository.findBySex("Male");
        if (animals.isEmpty()) {
            return new CustomResponse<>(
                    null,
                    true,
                    404,
                    "No se encontraron animales macho"
            );
        }
        return new CustomResponse<>(
                animals,
                false,
                200,
                "Animales macho encontrados"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Animals>> getPendingApprovalAnimals() {
        List<Animals> pendingAnimals = animalsRepository.findByApprovalStatus(ApprovalStatus.PENDING);
        return new CustomResponse<>( pendingAnimals,
                false,
                200,
                "Animales pendientes de aprobación obtenidos correctamente");
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Animals>> getApprovedAnimals() {
        List<Animals> pendingAnimals = animalsRepository.findByApprovalStatus(ApprovalStatus.APPROVED);
        return new CustomResponse<>( pendingAnimals,
                false,
                200,
                "Animales  aprobados correctamente");
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Boolean> approveOrRejectAnimal(UUID animalId,
                                                         ApprovalStatus approvalStatus,
                                                         String moderatorComment) {
        Optional<Animals> optionalAnimal = animalsRepository.findById(animalId);
        if (optionalAnimal.isPresent()) {
            Animals animal = optionalAnimal.get();
            animal.setApprovalStatus(approvalStatus);
            animal.setModeratorComment(moderatorComment);
            animalsRepository.save(animal);
            if (ApprovalStatus.APPROVED.equals(approvalStatus)) {
                try {
                    mailer.sendAcceptedRequest(animal.getRegister().getEmail(), animal.getRegister().getName());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
            return new CustomResponse<>(true, false, 200, "Animal aprobado/rechazado correctamente");
        } else {
            return new CustomResponse<>(false, true, 404, "Animal no encontrado");
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Animals> insert(Animals animal, List<MultipartFile> imageFiles) {
        try {
            Animals savedAnimal = animalsRepository.saveAndFlush(animal);
            List<AnimalImage> images = new ArrayList<>();
            for (MultipartFile file : imageFiles) {
                String imageUrl = cloudinaryService.uploadFile(file, "animal_images");
                AnimalImage animalImage = new AnimalImage();
                animalImage.setAnimal(savedAnimal);
                animalImage.setImageUrl(imageUrl);
                images.add(animalImage);
            }
            savedAnimal.setImages(images);

            animalImageRepository.saveAll(images);

            return new CustomResponse<>(savedAnimal, false, 200, "Animal registrado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse<>(null, true, 500, "Error al registrar el animal");
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Animals> update(UUID id, AnimalDto animalDto, List<MultipartFile> imageFiles) {
        try {
            Animals animal = animalsRepository.findById(id).orElseThrow(() -> new RuntimeException("Animal no encontrado"));

            animal.setNamePet(animalDto.getNamePet());
            animal.setLocation(animalDto.getLocation());
            animal.setTypePet(animalDto.getTypePet());
            animal.setRace(animalDto.getRace());
            animal.setPersonality(animalDto.getPersonality());
            animal.setSex(animalDto.getSex());
            animal.setSize(animalDto.getSize());
            animal.setWeight(animalDto.getWeight());
            animal.setAge(animalDto.getAge());
            animal.setColor(animalDto.getColor());
            animal.setSterilized(animalDto.getSterilized());
            animal.setDescription(animalDto.getDescription());
            animal.setApprovalStatus(animalDto.getApprovalStatus());
            animal.setModeratorComment(animalDto.getModeratorComment());

            Animals savedAnimal = animalsRepository.saveAndFlush(animal);

            if (imageFiles != null && !imageFiles.isEmpty()) {
                animalImageRepository.deleteById(id);

                List<AnimalImage> images = new ArrayList<>();
                for (MultipartFile file : imageFiles) {
                    String imageUrl = cloudinaryService.uploadFile(file, "animal_images");
                    AnimalImage animalImage = new AnimalImage();
                    animalImage.setAnimal(savedAnimal);
                    animalImage.setImageUrl(imageUrl);
                    images.add(animalImage);
                }
                savedAnimal.setImages(images);

                animalImageRepository.saveAll(images);
            }
            return new CustomResponse<>(savedAnimal, false, 200, "Animal editado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse<>(null, true, 500, "Error al editar el animal");
        }
    }


    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Boolean> delete(UUID id){
        if(!this.animalsRepository.existsById(id)){
            return new CustomResponse<>(
                    false,
                    true,
                    400,
                    "No encontrado"
            );
        }
        this.animalsRepository.deleteById(id);
        return new CustomResponse<>(
                true,
                false,
                200,
                "Eliminado correctamente"
        );
    }
}
