package com.gym.app.workout.service;

import com.gym.app.dto.WorkoutDto;
import com.gym.app.workout.entity.Workout;

import java.util.List;

public interface WorkoutService {
    List<WorkoutDto> getAllWorkouts();

    Workout getWorkoutById(Long id);

    List<WorkoutDto> getWorkoutsByTypeAndWeek(String type, String week, Long customerId);

    void saveWorkoutWithCustomer(List<WorkoutDto> workoutsDto, Long id);

    Workout saveWorkout(WorkoutDto workoutDto);

    void deleteWorkout(Long id);

    void deleteWorkoutCustomer(Long workoutId, Long customerId);

    void deleteWorkoutByWeek(String week, Long customerId);

    List<WorkoutDto> workoutOfCustomer(Long id);

    List<WorkoutDto> myProgramme();

    List<WorkoutDto> getWorkoutsForAthlete(String type, String week);
}
