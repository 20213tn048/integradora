package mx.edu.utez.mexprotec.models.animals;

import mx.edu.utez.mexprotec.models.animals.personality.Personality;
import mx.edu.utez.mexprotec.models.animals.race.Race;
import mx.edu.utez.mexprotec.models.animals.typePet.TypePet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnimalsRepository extends JpaRepository<Animals, UUID> {

    
    Optional<Animals> findById(UUID id);
    Animals getById(UUID id);
    List<Animals> findByApprovalStatus(ApprovalStatus approvalStatus);

    List<Animals> findByTypePet(TypePet typePet);
    List<Animals> findByRace(Race race);
    List<Animals> findByPersonality(Personality personality);
    List<Animals> findBySex(String sex);
    void deleteById(UUID id);
}
