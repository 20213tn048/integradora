package mx.edu.utez.mexprotec.models.animals.race;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RaceRepository extends JpaRepository<Race, UUID> {

    Optional<Race> findById(UUID id);
    Race getById(UUID id);
    Optional<Race> findByRacePet(String racePet);

}
