package mx.edu.utez.mexprotec.segurity.controller;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import mx.edu.utez.mexprotec.models.users.Users;
import mx.edu.utez.mexprotec.segurity.jwt.JwtProvider;
import mx.edu.utez.mexprotec.services.UserService;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin()
public class AuthController {

    private final AuthenticationManager manager;
    private final JwtProvider provider;
    private final JavaMailSender javaMailSender;
    private final UserService service;
    
    @Autowired
    private AuthController(AuthenticationManager manager, JwtProvider provider, JavaMailSender javaMailSender, UserService service){
        this.manager = manager;
        this.provider = provider;
        this.javaMailSender = javaMailSender;
        this.service = service;
    }

    @Value("${spring.mail.username}")
    private String mailFrom;

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<Object>> login(
            @Valid @RequestBody LoginDto login
    ) {
        try {
            Authentication authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            login.getEmail(), login.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = provider.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", userDetails);
            return new ResponseEntity<>(
                    new CustomResponse<>(data, false, 200, "OK"),
                    HttpStatus.OK
            );
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(
                    new CustomResponse<>(null, true, 400, "No se pudo iniciar sesión"),
                    HttpStatus.OK
            );
        }
    }

    @PostMapping("/reset-password")
        public ResponseChangeDto requestPasswordReset(@Valid @RequestBody RequestChangeDto dataRequest)
            throws MessagingException {
        String host = "http://localhost:5173/#/auth/changePassword";
        Users user = service.findByEmail(dataRequest.getEmail());

        if (user == null) {
            ResponseChangeDto response = new ResponseChangeDto();
            response.setMessage("Ocurrió un error al enviar el correo electrónico.");
            response.setError(true);
            return response;
        } else {
            String token = provider.generatePasswordResetToken(user.getEmail());

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setFrom(mailFrom);
            helper.setSubject("Recupera tu contraseña");
            helper.setText("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                    "  </head>\n" +
                    "\n" +
                    "  <body style=\"font-family: 'Segoe UI', sans-serif; text-align: center\">\n" +
                    "    <div style=\"width: 100%\">\n" +
                    "      <div style=\"margin-top: 20px; display: flex; justify-content: center\">\n" +
                    "        <div style=\"width: 550px; margin: auto\">\n" +
                    "          <div\n" +
                    "            style=\"\n" +
                    "              border: 2px solid #ccc0c1;\n" +
                    "              border-radius: 0.5rem;\n" +
                    "              overflow: hidden;\n" +
                    "            \"\n" +
                    "          >\n" +
                    "            <img\n" +
                    "              src=\"https://assets-global.website-files.com/63634f4a7b868a399577cf37/63ceba1ae7b26aa4ad28478f_adopcion%20de%20razas%20de%20perros%20pequen%CC%83as.jpg\"\n" +
                    "              style=\"width: 100%\"\n" +
                    "              alt=\"mexpet\"\n" +
                    "            />\n" +
                    "            <div style=\"background-color: #b53439; height: 35px\"></div>\n" +
                    "            <div style=\"padding: 15px\">\n" +
                    "              <p style=\"font-weight: bold; font-size: 1.5rem; margin-top: -8px\">\n" +
                    "                Recuperar contraseña\n" +
                    "              </p>\n" +
                    "              <p>\n" +
                    "                Hola " + user.getNameUser() + ", parece que has olvidado tu contraseña. No te\n" +
                    "                preocupes, haz clic <a href='" + host + "/?token=" + token + "' style=\"color: #b53439; font-weight: bold;\">aquí</a> para cambiarla.\n" +
                    "              </p>\n" +
                    "              <p>\n" +
                    "                <small style=\"font-style: italic; color: #6c757d\"\n" +
                    "                  >Este mensaje es automático. No es necesario responder.</small\n" +
                    "                >\n" +
                    "              </p>\n" +
                    "            </div>\n" +
                    "          </div>\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>\n", true);
            javaMailSender.send(message);

            ResponseChangeDto response = new ResponseChangeDto();
            response.setMessage("Se ha enviado un correo electrónico a " +
                    user.getEmail() + " con las instrucciones para restablecer tu contraseña.");
            response.setError(false);
            return response;
        }
    }

    @PostMapping("/reset-password/confirm")
    public ResponseChangeDto resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordData) {
        String user = provider.getEmailFromPasswordResetToken(resetPasswordData.getToken());
        if (user == null) {
            ResponseChangeDto response = new ResponseChangeDto();
            response.setMessage("El token no es válido o ha expirado.");
            response.setError(true);
            return response;
        } else {
            Users users = service.findByEmail(user);
            users.setPassword(resetPasswordData.getPassword());
            CustomResponse<Boolean> result = service.updatePassword(users);
            if (!result.getError()) {
                ResponseChangeDto response = new ResponseChangeDto();
                response.setMessage("Contraseña actualizada correctamente.");
                response.setError(false);
                return response;

            }else {
                ResponseChangeDto response = new ResponseChangeDto();
                response.setMessage("Ocurrió un error al actualizar la contraseña.");
                response.setError(true);
                return response;
            }
        }
    }

}
