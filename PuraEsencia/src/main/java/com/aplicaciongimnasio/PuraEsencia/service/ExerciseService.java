package com.aplicaciongimnasio.PuraEsencia.service;

import com.aplicaciongimnasio.PuraEsencia.model.Exercise;
import com.aplicaciongimnasio.PuraEsencia.model.User;
import com.aplicaciongimnasio.PuraEsencia.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    // Crear un nuevo ejercicio
    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    // Obtener un ejercicio por ID
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con ID: " + id));
    }

    // Otros métodos relacionados con ejercicios (si los necesitas)
    @Transactional
    public boolean deleteExerciseById(Long id) {
        Optional<Exercise> exerciseOptional = exerciseRepository.findById(id);
        if (exerciseOptional.isPresent()) {
            exerciseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Exercise updateExercise(Long id, Exercise updatedExercise) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));
        // Actualizar los campos del ejercicio
        if (updatedExercise.getName() != null) {
            exercise.setName(updatedExercise.getName());
        }
        if (updatedExercise.getDescription() != null) {
            exercise.setDescription(updatedExercise.getDescription());
        }
        return exerciseRepository.save(exercise);
    }
}