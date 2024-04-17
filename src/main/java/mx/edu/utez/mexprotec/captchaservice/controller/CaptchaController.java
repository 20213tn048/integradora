package mx.edu.utez.mexprotec.captchaservice.controller;

import mx.edu.utez.mexprotec.captchaservice.dto.CaptchaResponse;
import mx.edu.utez.mexprotec.captchaservice.service.CaptchaService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:5173/",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS}
)
@RequestMapping("/api/captcha")
public class CaptchaController {
    private CaptchaService captchaService;

    @PostMapping("/verify-captcha")
    public CaptchaResponse verifyCaptcha(@RequestParam("solution") String solution) {
        return captchaService.verifyCaptcha(solution);
    }
}
