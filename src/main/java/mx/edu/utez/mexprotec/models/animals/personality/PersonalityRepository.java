package mx.edu.utez.mexprotec.models.animals.personality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonalityRepository extends JpaRepository<Personality, UUID> {

    Optional<Personality> findById(UUID id);
    Personality getById(UUID id);
    Optional<Personality> findByPersonalityPet(String personalityPet);
}
