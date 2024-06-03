package com.gym.app.workout.controller;

import com.gym.app.dto.WorkoutDto;
import com.gym.app.workout.entity.Workout;
import com.gym.app.workout.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/get")
    public List<WorkoutDto> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    @GetMapping("/{id}")
    public Workout getWorkoutById(@PathVariable Long id) {
        return workoutService.getWorkoutById(id);
    }

    @PostMapping("/add/workout")
    public ResponseEntity<String> createWorkout(@RequestBody WorkoutDto workoutDto) {
        workoutService.saveWorkout(workoutDto);
        return ResponseEntity.ok("Added workout");
    }

    /*@PutMapping("/update/{id}")
    public Workout updateWorkout(@PathVariable Long id, @RequestBody Workout workout) {
        Workout existingWorkout = workoutService.getWorkoutById(id);
        if (existingWorkout != null) {
            workout.setId(id);
            return workoutService.saveWorkout(workout);
        }
        return null;
    }*/

    @DeleteMapping("/delete/{id}")
    public void deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
    }
}