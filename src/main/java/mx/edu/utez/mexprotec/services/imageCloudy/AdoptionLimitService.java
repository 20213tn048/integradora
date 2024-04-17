package mx.edu.utez.mexprotec.services.imageCloudy;

import mx.edu.utez.mexprotec.models.adoption.AdoptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdoptionLimitService {

    private final AdoptionRepository adoptionRepository;
    
    @Autowired
    private AdoptionLimitService(AdoptionRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }
    
    public boolean isAdoptionLimitReached() {
        LocalDate today = LocalDate.now();
        long adoptionCount = adoptionRepository.countByDate(today);
        int adoptionLimit = 2;
        return adoptionCount >= adoptionLimit;
    }
}

