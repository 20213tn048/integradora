package mx.edu.utez.mexprotec.captchaservice.service;

import mx.edu.utez.mexprotec.captchaservice.dto.CaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CaptchaService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${FRIENDLYCAPTCHA.CAPTCHAKEY}")
    private String captchaKey;
    @Value("${FRIENDLYCAPTCHA.SITEKEY}")
    private String siteKey;

    public CaptchaResponse verifyCaptcha(String solution) {
        String url = "https://api.friendlycaptcha.com/api/v1/siteverify";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("solution", solution);
        requestBody.put("secret", captchaKey);
        requestBody.put("sitekey", siteKey);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<CaptchaResponse> responseEntity =
                restTemplate.postForEntity(
                        url,
                        requestEntity,
                        CaptchaResponse.class);
        return responseEntity.getBody();
    }
}
