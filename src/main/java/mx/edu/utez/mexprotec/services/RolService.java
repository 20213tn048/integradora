package mx.edu.utez.mexprotec.services;

import mx.edu.utez.mexprotec.models.rol.Rol;
import mx.edu.utez.mexprotec.models.rol.RolRepository;
import mx.edu.utez.mexprotec.models.users.Users;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RolService {
    private final UserService userService;
    private final RolRepository rolRepository;

    @Autowired
    private RolService(RolRepository rolRepository, UserService userService) {
        this.rolRepository = rolRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = false)
    public void createRoles() {
        try {
            List<Rol> roles = this.rolRepository.findAll();
            if (roles.isEmpty()) {

                Rol rolAdmin = new Rol();
                rolAdmin.setStatus(true);
                rolAdmin.setNrol("ADMIN");
                this.rolRepository.save(rolAdmin);

                Rol rolModerador = new Rol();
                rolModerador.setStatus(true);
                rolModerador.setNrol("MODERADOR");
                this.rolRepository.save(rolModerador);

                Rol rolCliente = new Rol();
                rolCliente.setStatus(true);
                rolCliente.setNrol("CLIENTE");
                this.rolRepository.save(rolCliente);

                rolAdmin = this.rolRepository.findByNrol("ADMIN");
                rolModerador = this.rolRepository.findByNrol("MODERADOR");
                rolCliente = this.rolRepository.findByNrol("CLIENTE");

                Users adminUser = new Users();
                adminUser.setNameUser("MichKwon");
                adminUser.setName("Michelle");
                adminUser.setLastname("Estrada");
                adminUser.setLastnameMatern("Hernández");
                adminUser.setEmail("20213tn011@utez.edu.mx");
                adminUser.setPhoneNumber("7774857215");
                adminUser.setLocalitation("Cuernavaca");
                adminUser.setPassword("OsmichKwon05#");
                adminUser.setRol(rolAdmin);
                this.userService.insert(adminUser);

                Users modeUser = new Users();
                modeUser.setNameUser("RichieRG");
                modeUser.setName("Ricardo");
                modeUser.setLastname("Rodríguez");
                modeUser.setLastnameMatern("Trejo");
                modeUser.setEmail("20203tn145@utez.edu.mx");
                modeUser.setPhoneNumber("7777654321");
                modeUser.setLocalitation("Emiliano Zapata");
                modeUser.setPassword("Contraseña!123");
                modeUser.setRol(rolModerador);
                this.userService.insert(modeUser);

                Users clientUser = new Users();
                clientUser.setNameUser("NattyAraujo");
                clientUser.setName("Natalia");
                clientUser.setLastname("García");
                clientUser.setLastnameMatern("Araujo");
                clientUser.setEmail("20213tn015@utez.edu.mx");
                clientUser.setPhoneNumber("7771234567");
                clientUser.setLocalitation("Cuernavaca");
                clientUser.setPassword("Natty.Araujo#");
                clientUser.setRol(rolCliente);
                this.userService.insert(clientUser);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Rol>> getAllRoles() {
        try {
            List<Rol> roles = this.rolRepository.findAll();
            return new CustomResponse<>(
                    roles,
                    false,
                    200,
                    "OK"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse<>(
                    null,
                    true,
                    500,
                    "Error al obtener roles"
            );
        }
    }
}
