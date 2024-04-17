package mx.edu.utez.mexprotec.models.animals.typePet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TypePetRepository extends JpaRepository<TypePet, UUID> {

    Optional<TypePet> findById(UUID id);
    TypePet getById(UUID id);
    Optional<TypePet> findByType(String type);

}

