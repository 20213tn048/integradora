package mx.edu.utez.mexprotec.services.category;

import mx.edu.utez.mexprotec.models.animals.race.Race;
import mx.edu.utez.mexprotec.models.animals.race.RaceRepository;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RaceService {

    private final RaceRepository raceRepository;
    
    @Autowired
    private RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Race>> getAll(){
        return new CustomResponse<>(
                this.raceRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<Race> getOne(UUID id){
        Optional<Race> optional = this.raceRepository.findById(id);
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

    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Race> insert(Race race){
        Optional<Race> existingRace = raceRepository.findByRacePet(race.getRacePet());
        if (existingRace.isPresent()) {
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "La raza ya existe"
            );
        } else {
            return new CustomResponse<>(
                    this.raceRepository.saveAndFlush(race),
                    false,
                    200,
                    "Registrado correctamente"
            );
        }
    }

    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Race> update(Race race){
        if(!this.raceRepository.existsById(race.getId()))
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "No encontrado"
            );
        return new CustomResponse<>(
                this.raceRepository.saveAndFlush(race),
                false,
                200,
                "Actualizado correctamente"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Boolean> deleteById(UUID id) {
        if (!this.raceRepository.existsById(id)) {
            return new CustomResponse<>(
                    false,
                    true,
                    400,
                    "No encontrado"
            );
        }

        this.raceRepository.deleteById(id);
        return new CustomResponse<>(
                true,
                false,
                200,
                "Raza eliminada correctamente"
        );
    }
}
