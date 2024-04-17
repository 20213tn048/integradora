package mx.edu.utez.mexprotec.services;

import mx.edu.utez.mexprotec.models.history.History;
import mx.edu.utez.mexprotec.models.history.HistoryRepository;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    @Autowired
    private HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<History>> getAll(){
        return new CustomResponse<>(
                this.historyRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public  CustomResponse<List<History>> getAllActive(){
        return new CustomResponse<>(
                this.historyRepository.findAllByStatus(true),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public  CustomResponse<List<History>> getAllInactive(){
        return new CustomResponse<>(
                this.historyRepository.findAllByStatus(false),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<History> getOne(UUID id){
        Optional<History> optional = this.historyRepository.findById(id);
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
    public CustomResponse<History> insert(History history){
        return new CustomResponse<>(
                this.historyRepository.saveAndFlush(history),
                false,
                200,
                "Registrado correctamente"
        );
    }

    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<History> update(History history){
        if(!this.historyRepository.existsById(history.getId()))
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "No encontrado"
            );
        return new CustomResponse<>(
                this.historyRepository.saveAndFlush(history),
                false,
                200,
                "Actualizado correctamente"
        );
    }

    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Boolean> changeStatus(History history){
        if(!this.historyRepository.existsById(history.getId())){
            return new CustomResponse<>(
                    false,
                    true,
                    400,
                    "No encontrado"
            );
        }
        return new CustomResponse<>(
                this.historyRepository.updateStatusById(
                        history.getStatus(), history.getId()
                ) == 1,
                false,
                200,
                "¡Se ha cambiado el status correctamente!"
        );
    }

    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Boolean> delete(UUID id){
        if(!this.historyRepository.existsById(id)){
            return new CustomResponse<>(
                    false,
                    true,
                    400,
                    "No encontrado"
            );
        }
        this.historyRepository.deleteById(id);
        return new CustomResponse<>(
                true,
                false,
                200,
                "Eliminado correctamente"
        );
    }
}
