package mx.edu.utez.mexprotec.models.image.animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AnimalImageRepository extends JpaRepository<AnimalImage, UUID> {
}

