package mx.edu.utez.mexprotec.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service

public class Mailer {

    private final JavaMailSender javaMailSender;
    
    @Autowired
    private Mailer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    
    @Value("${spring.mail.username}")
    public String mailFrom;

    public boolean sendEmailWelcome(String email, String name, String affair) throws MessagingException {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom(mailFrom);
            helper.setSubject(affair);

            //Plantilla del email de bienvenida
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
                    "              alt=\"Libro\"\n" +
                    "            />\n" +
                    "            <div style=\"background-color: #b53439; height: 35px\"></div>\n" +
                    "            <div style=\"padding: 15px\">\n" +
                    "              <p style=\"font-weight: bold; font-size: 1.5rem; margin-top: -8px;\">\n" +
                    "                ¡Bienvenido " + name + "!\n" +
                    "              </p>\n" +
                    "<p>\n" +
                    "Estamos emocionados de tenerte con nosotros, listo para entrar\n" +
                    "en el maravilloso mundo de la adopción de animales. Aquí, cada adopción es una\n" +
                    "oportunidad para cambiar vidas y cada animal es un compañero en potencia listo para\n" +
                    "llenar tu vida de amor y alegría. Prepárate para embarcarte en una aventura donde cada\n" +
                    "mascota tiene una historia única, cada encuentro es una conexión especial y cada hogar es\n" +
                    "un refugio de amor incondicional. ¡Gracias por unirte a nosotros en esta hermosa misión de\n" +
                    "brindarles un hogar a aquellos que lo necesitan!\n" +
                    "</p>\n" +
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
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendAcceptedRequest(String email, String name) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom(mailFrom);
            helper.setSubject("Solicitud procesada");
            String mensaje = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                    "  </head>\n" +
                    "  <style>\n" +
                    "    td {\n" +
                    "      border: 1px solid #ddd;\n" +
                    "      padding: 8px;\n" +
                    "    }\n" +
                    "  </style>\n" +
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
                    "              src=\"https://demo.stripocdn.email/content/guids/d0dee27c-b951-4be2-9e65-fe5d431243a4/images/booksg5a3638f0519201633971795.jpg\"\n" +
                    "              style=\"width: 100%\"\n" +
                    "              alt=\"mexpet\"\n" +
                    "            />\n" +
                    "            <div style=\"background-color: #b53439; height: 35px\"></div>\n" +
                    "            <div style=\"padding: 15px\">\n" +
                    "              <p style=\"font-weight: bold; font-size: 1.5rem; margin-top: -8px\">\n" +
                    "                Solicitud Procesada\n" +
                    "              </p>\n" +
                    "              <p>Hola " + name + ", tu solicitud ha sido procesada.</p>\n" +
                    "              <p>Gracias por utilizar nuestra aplicación.</p>\n" +
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
                    "</html>\n";
            helper.setText(mensaje, true);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendDismissedRequest(String email, String name) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom(mailFrom);
            helper.setSubject("Solicitud procesada");
            String mensaje = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                    "  </head>\n" +
                    "  <style>\n" +
                    "    td {\n" +
                    "      border: 1px solid #ddd;\n" +
                    "      padding: 8px;\n" +
                    "    }\n" +
                    "  </style>\n" +
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
                    "              src=\"https://demo.stripocdn.email/content/guids/d0dee27c-b951-4be2-9e65-fe5d431243a4/images/booksg5a3638f0519201633971795.jpg\"\n" +
                    "              style=\"width: 100%\"\n" +
                    "              alt=\"mexpet\"\n" +
                    "            />\n" +
                    "            <div style=\"background-color: #b53439; height: 35px\"></div>\n" +
                    "            <div style=\"padding: 15px\">\n" +
                    "              <p style=\"font-weight: bold; font-size: 1.5rem; margin-top: -8px\">\n" +
                    "                Solicitud Procesada\n" +
                    "              </p>\n" +
                    "              <p>Hola " + name + ", tu solicitud no ha sido procesada. Vuelve  a intentar</p>\n" +
                    "              <p>Gracias por utilizar nuestra aplicación.</p>\n" +
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
                    "</html>\n";
            helper.setText(mensaje, true);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
