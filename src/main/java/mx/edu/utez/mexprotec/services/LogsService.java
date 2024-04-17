package mx.edu.utez.mexprotec.services;

import mx.edu.utez.mexprotec.models.logs.Logs;
import mx.edu.utez.mexprotec.models.logs.LogsRepository;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.util.List;

@Service
@Transactional
public class LogsService {

    private final LogsRepository repository;

    private String ok = "Correcto";

    @Autowired
    private LogsService(LogsRepository repository){
        this.repository = repository;
    }
    
    @Transactional(readOnly = true)
    public CustomResponse<Logs> getById(String id) throws SQLException {
        return new CustomResponse<>(this.repository.findById(id).orElse(null), false, 200, ok);
    }
    @Transactional(readOnly = true)
    public CustomResponse<List<Logs>> getAll() throws SQLException {
        return new CustomResponse<>(this.repository.findAll(), false, 200, ok);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Logs> save(Logs bitacora) throws SQLException {
        return new CustomResponse<>(this.repository.save(bitacora), false, 200, ok);
    }
}
