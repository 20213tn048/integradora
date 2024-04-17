package mx.edu.utez.mexprotec.services.category;


import mx.edu.utez.mexprotec.models.animals.typePet.TypePet;
import mx.edu.utez.mexprotec.models.animals.typePet.TypePetRepository;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TypePetService {

    private final TypePetRepository typeRepository;
    
    @Autowired
    private TypePetService(TypePetRepository typeRepository){
        this.typeRepository = typeRepository;
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<TypePet>> getAll(){
        return new CustomResponse<>(
                this.typeRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<TypePet> getOne(UUID id){
        Optional<TypePet> optional = this.typeRepository.findById(id);
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
    public CustomResponse<TypePet> insert(TypePet type){
        Optional<TypePet> existingType = typeRepository.findByType(type.getType());
        if (existingType.isPresent()) {
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "El tipo de animal ya existe"
            );
        } else {
            return new CustomResponse<>(
                    this.typeRepository.saveAndFlush(type),
                    false,
                    200,
                    "Tipo de animal insertado correctamente"
            );
        }
    }

    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<TypePet> update(TypePet type){
        if(!this.typeRepository.existsById(type.getId()))
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "No encontrado"
            );
        return new CustomResponse<>(
                this.typeRepository.saveAndFlush(type),
                false,
                200,
                "Actualizado correctamente"
        );
    }
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Boolean> deleteById(UUID id) {
        if (!this.typeRepository.existsById(id)) {
            return new CustomResponse<>(
                    false,
                    true,
                    400,
                    "No encontrado"
            );
        }

        this.typeRepository.deleteById(id);
        return new CustomResponse<>(
                true,
                false,
                200,
                "Tipo de animal eliminado correctamente"
        );
    }
}
