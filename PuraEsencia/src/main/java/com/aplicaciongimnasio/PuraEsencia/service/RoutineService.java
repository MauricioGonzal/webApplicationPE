package com.aplicaciongimnasio.PuraEsencia.service;

import com.aplicaciongimnasio.PuraEsencia.model.Exercise;
import com.aplicaciongimnasio.PuraEsencia.model.Routine;
import com.aplicaciongimnasio.PuraEsencia.repository.ExerciseRepository;
import com.aplicaciongimnasio.PuraEsencia.repository.RoutineRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    // Crear una rutina con ejercicios
    @Transactional
    public Routine createRoutineWithExercises(Routine routine, List<Long> exerciseIds) {
        Set<Exercise> exercises = new HashSet<>();

        // Buscar los ejercicios a partir de los IDs proporcionados
        for (Long exerciseId : exerciseIds) {
            Exercise exercise = exerciseRepository.findById(exerciseId)
                    .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con ID: " + exerciseId));
            exercises.add(exercise);
        }

        routine.setExercises(exercises);  // Asigna los ejercicios a la rutina

        // Guarda la rutina con los ejercicios asociados
        return routineRepository.save(routine);
    }

    // Obtener una rutina por ID
    public Routine getRoutineById(Long id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada con ID: " + id));
    }

    @Transactional
    public Routine addExercisesToRoutine(Long routineId, Set<Long> exerciseIds) {
        // Buscar la rutina existente
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

        // Buscar los ejercicios por sus IDs
        Set<Exercise> exercisesToAdd = new HashSet<>(exerciseRepository.findAllById(exerciseIds));

        if (exercisesToAdd.isEmpty()) {
            throw new RuntimeException("No se encontraron ejercicios con los IDs proporcionados");
        }

        // Agregar los ejercicios a la rutina existente
        routine.getExercises().addAll(exercisesToAdd);

        // Guardar y devolver la rutina actualizada
        return routineRepository.save(routine);
    }
}