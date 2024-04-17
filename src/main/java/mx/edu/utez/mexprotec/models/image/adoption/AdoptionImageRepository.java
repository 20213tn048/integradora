package mx.edu.utez.mexprotec.models.image.adoption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdoptionImageRepository extends JpaRepository<AdoptionImage, UUID> {
}
