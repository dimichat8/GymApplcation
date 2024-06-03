package com.gym.app.workout.service;

import com.gym.app.dto.WorkoutDto;
import com.gym.app.workout.entity.Workout;

import java.util.List;

public interface WorkoutService {
    List<WorkoutDto> getAllWorkouts();

    Workout getWorkoutById(Long id);

    Workout saveWorkout(WorkoutDto workoutDto);

    void deleteWorkout(Long id);
}
