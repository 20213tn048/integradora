package mx.edu.utez.mexprotec.config.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;
    
    @Autowired
    private CloudinaryService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file, String folderName) {
        try {
            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("El tamaño de la imagen excede el límite de 10 MB.");
            }
            Map options = new HashMap<>();
            options.put("folder", folderName);
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            return (String) uploadedFile.get("url");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

