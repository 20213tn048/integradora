package mx.edu.utez.mexprotec.models.rol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RolRepository extends JpaRepository<Rol, UUID> {
    Rol getByIdRol(UUID id);

    Rol findByNrol(String rol);
}
