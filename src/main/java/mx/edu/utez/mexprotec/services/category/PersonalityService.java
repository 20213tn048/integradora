package mx.edu.utez.mexprotec.services.category;

import org.springframework.transaction.annotation.Transactional;
import mx.edu.utez.mexprotec.models.animals.personality.Personality;
import mx.edu.utez.mexprotec.models.animals.personality.PersonalityRepository;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonalityService {

    private final PersonalityRepository personalityRepository;
    
    @Autowired
    private PersonalityService(PersonalityRepository personalityRepository){
        this.personalityRepository = personalityRepository;
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Personality>> getAll(){
        return new CustomResponse<>(
                this.personalityRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<Personality> getOne(UUID id){
        Optional<Personality> optional = this.personalityRepository.findById(id);
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

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Personality> insert(Personality personality) {
        Optional<Personality> existingPersonality = personalityRepository.findByPersonalityPet(personality.getPersonalityPet());
        if (existingPersonality.isPresent()) {
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "La personalidad ya existe"
            );
        } else {
            return new CustomResponse<>(
                    personalityRepository.saveAndFlush(personality),
                    false,
                    200,
                    "Registrado correctamente"
            );
        }
    }
    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Personality> update(Personality personality){
        if(!this.personalityRepository.existsById(personality.getId()))
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "No encontrado"
            );
        return new CustomResponse<>(
                this.personalityRepository.saveAndFlush(personality),
                false,
                200,
                "Actualizado correctamente"
        );
    }
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Boolean> deleteById(UUID id) {
        if (!this.personalityRepository.existsById(id)) {
            return new CustomResponse<>(
                    false,
                    true,
                    400,
                    "No encontrado"
            );
        }

        this.personalityRepository.deleteById(id);
        return new CustomResponse<>(
                true,
                false,
                200,
                "Personalidad eliminada correctamente"
        );
    }

}
