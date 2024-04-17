package mx.edu.utez.mexprotec.models.action;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActionsRepository extends JpaRepository<Actions, UUID> {

    Optional<Actions> findById(UUID id);
    Actions getById(UUID id);
}
