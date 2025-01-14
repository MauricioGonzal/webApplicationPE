package com.aplicaciongimnasio.PuraEsencia.service;

import com.aplicaciongimnasio.PuraEsencia.model.Routine;
import com.aplicaciongimnasio.PuraEsencia.model.User;
import com.aplicaciongimnasio.PuraEsencia.repository.RoutineRepository;
import com.aplicaciongimnasio.PuraEsencia.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Este método crea un nuevo usuario con la contraseña encriptada
    public User createUser(User user) {
        // Encriptar la contraseña antes de guardarla
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        // Asigna la contraseña encriptada al usuario
        user.setPassword(encryptedPassword);

        // Guarda el usuario en la base de datos (suponiendo que tienes un repositorio)
        return userRepository.save(user);
    }

    @Transactional
    public boolean deleteUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            userRepository.deleteByEmail(email);  // Elimina el usuario por username
            return true;
        }
        return false;  // Si no se encuentra, retorna false
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(String email, User updatedUser) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Actualizar los campos del usuario
        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }
        if (updatedUser.getPassword() != null) {
            // Encriptar la nueva contraseña
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userRepository.save(user);
    }

    public User assignRoutineToUser(Long userId, Long routineId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        user.setRoutine(routine);
        return userRepository.save(user);
    }

    public Routine getUserRoutine(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return user.getRoutine();
    }
}
