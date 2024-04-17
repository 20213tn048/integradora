package mx.edu.utez.mexprotec.services;

import mx.edu.utez.mexprotec.models.rol.Rol;
import mx.edu.utez.mexprotec.models.rol.RolRepository;
import mx.edu.utez.mexprotec.models.users.Users;
import mx.edu.utez.mexprotec.models.users.UsersRepository;
import mx.edu.utez.mexprotec.utils.CustomResponse;
import mx.edu.utez.mexprotec.utils.Mailer;
// import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    
    private final UsersRepository usersRepository;
    private final RolRepository rolRepository;
    private final Mailer mailer;
    private final PasswordEncoder encoder;

    @Autowired
    private UserService(UsersRepository usersRepository, RolRepository rolRepository, Mailer mailer, PasswordEncoder encoder) { 
        this.usersRepository = usersRepository;
        this.rolRepository = rolRepository;
        this.mailer = mailer;
        this.encoder = encoder;
    }
    // @Lazy
    
    @Transactional(readOnly = true)
    public CustomResponse<List<Users>> getAll() {
        return new CustomResponse<>(
                this.usersRepository.findAll(),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<Users> getOne(UUID id) {
        Optional<Users> optionalUser = this.usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            return new CustomResponse<>(optionalUser.get(), false, 200, "Usuario encontrado");
        } else {
            return new CustomResponse<>(null, true, 404, "Usuario no encontrado");
        }
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Users>> getActive() {
        return new CustomResponse<>(
                this.usersRepository.findAllByStatus(true),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Users>> getInactive() {
        return new CustomResponse<>(
                this.usersRepository.findAllByStatus(false),
                false,
                200,
                "Ok"
        );
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Users>> getByRole(String roleName) {
        List<Users> users = this.usersRepository.findByRol_Nrol(roleName);
        if (!users.isEmpty()) {
            return new CustomResponse<>(users, false, 200, "Usuarios encontrados por rol");
        } else {
            return new CustomResponse<>(null, true, 404, "No se encontraron usuarios con ese rol");
        }
    }

    @Transactional(readOnly = true)
    public CustomResponse<List<Users>> getUsersByAdminAndModeratorRoles() {
        List<String> roles = Arrays.asList("ADMIN", "MODERADOR");
        List<Users> users = this.usersRepository.findAllByRol_NrolIn(roles);
        if (!users.isEmpty()) {
            return new CustomResponse<>(users, false, 200, "Usuarios encontrados por roles ADMIN y MODERADOR");
        } else {
            return new CustomResponse<>(null, true, 404, "No se encontraron usuarios con los roles especificados");
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Users> insert(Users user) {
        if (this.usersRepository.findByEmailAndActivo(user.getEmail()) == null) {
            if (user.getRol() != null) {
                if (user.getRol().getIdRol() == null) {
                    user.getRol().setStatus(true);
                    Rol existingRol = rolRepository.findByNrol(user.getRol().getNrol());
                    if (existingRol != null) {
                        user.setRol(existingRol);
                    } else {
                        Rol persistedRol = rolRepository.save(user.getRol());
                        user.setRol(persistedRol);
                    }
                }
                String phoneNumber = user.getPhoneNumber();
                if (phoneNumber != null && phoneNumber.length() < 12) {
                    String password = user.getPassword();
                    if (password != null && password.length() >= 10 && containsUppercase(password) && containsSpecialCharacter(password)) {
                        user.setPassword(
                                this.encoder.encode(user.getPassword())
                        );
                        user.setStatus(true);
                        Users userSave = this.usersRepository.save(user);
                        try {
                            this.mailer.sendEmailWelcome(user.getEmail(), user.getName(), "¡Te damos la bienvenida MexPet!");
                        } catch (Exception e) {
                            return new CustomResponse<>(
                                    null, true, 400, "Ocurrió un error al enviar el correo"
                            );
                        }
                        return new CustomResponse<>(
                                userSave, false, 200, "Usuario registrado correctamente"
                        );
                    } else {
                        return new CustomResponse<>(null, true, 400, "La contraseña no cumple con los requisitos mínimos");
                    }
                } else {
                    return new CustomResponse<>(null, true, 400, "El teléfono no es válido");
                }
            } else {
                return new CustomResponse<>(
                        null, true, 400, "No se encontró el rol"
                );
            }
        } else {
            return new CustomResponse<>(
                    null, true, 400, "El correo ya está registrado"
            );
        }
    }
    
    @Transactional(rollbackFor =  {SQLException.class})
    public CustomResponse<Users> update(Users users){
        if(!this.usersRepository.existsById(users.getId()))
            return new CustomResponse<>(
                    null,
                    true,
                    400,
                    "No encontrado"
            );
        return new CustomResponse<>(
                this.usersRepository.saveAndFlush(users),
                false,
                200,
                "Actualizado correctamente"
        );
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Boolean> changeUserStatus(UUID id, Boolean newStatus) {
        try {
            Optional<Users> optionalUser = this.usersRepository.findById(id);
            if (optionalUser.isPresent()) {
                Users user = optionalUser.get();
                user.setStatus(newStatus);
                this.usersRepository.saveAndFlush(user);
                return new CustomResponse<>(true, false, 200, "Estado de usuario actualizado correctamente");
            } else {
                return new CustomResponse<>(null, true, 404, "Usuario no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse<>(null, true, 500, "Error al cambiar estado de usuario");
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Boolean> updatePassword(Users user) {
        Users temp = this.usersRepository.findByRolAndUserAndActivo(user.getId(), user.getRol().getIdRol());
        if (this.usersRepository.existsById(user.getId()) && user.getStatus() && temp != null) {
            String password = user.getPassword();
            if (password != null && password.length() >= 10 && containsUppercase(password) && containsSpecialCharacter(password)) {
                user.setPassword(
                        encoder.encode(user.getPassword())
                );
                this.usersRepository.saveAndFlush(user);
                return new CustomResponse<>(
                        true, false, 200, "user actualizado correctamente"
                );
            } else {
                return new CustomResponse<>(null, true, 400, "La contraseña no cumple con los requisitos mínimos");
            }
        } else {
            return new CustomResponse<>(
                    null, true, 400, "No se encontro el user"
            );
        }
    }


    @Transactional(readOnly = true)
    public Users findByEmail(String correo) {
        return this.usersRepository.findByEmailAndActivo(correo);
    }

    private boolean containsUppercase(String password) {
        return password.matches(".*[A-Z].*");
    }

    private boolean containsSpecialCharacter(String password) {
        return password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

}
