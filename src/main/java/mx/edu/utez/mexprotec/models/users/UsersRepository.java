package mx.edu.utez.mexprotec.models.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    @Modifying
    @Query("UPDATE Users u SET u.status = :status WHERE u.id = :id")
    void updateStatus(@Param("status") boolean status, @Param("id") UUID id);
    @Override
    Optional<Users> findById(UUID uuid);
    List<Users> findAllByRol_NrolIn(List<String> nrol);
    List<Users> findAllByStatus(Boolean status);
    List<Users> findByRol_Nrol(String nrol);
    Users getById(UUID id);

    @Query(value = "SELECT * FROM users WHERE status=true AND id = :id", nativeQuery = true)
    Users findByIdAndActivo(@Param("id") UUID id);

    @Query(value = "SELECT * FROM users WHERE status=true AND email = :email", nativeQuery = true)
    Users findByEmailAndActivo(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE status=true AND id = :id AND id_rol = :id_rol", nativeQuery = true)
    Users findByRolAndUserAndActivo(@Param("id") UUID id, @Param("id_rol") UUID id_rol);

}
