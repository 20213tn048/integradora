package mx.edu.utez.mexprotec.models.adoption;

import mx.edu.utez.mexprotec.models.animals.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, UUID> {

    Optional<Adoption> findById(UUID id);
    Adoption getById(UUID id);
    List<Adoption> findByApprovalStatus(ApprovalStatus approvalStatus);
    @Query("SELECT COUNT(a) FROM Adoption a WHERE DATE(a.creationDate) = :date")
    long countByDate(@Param("date") LocalDate date);

    void deleteById(UUID id);
}
