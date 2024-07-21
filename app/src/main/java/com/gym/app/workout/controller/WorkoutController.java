package com.gym.app.workout.controller;

import com.gym.app.dto.WorkoutDto;
import com.gym.app.workout.entity.Workout;
import com.gym.app.workout.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
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

    @GetMapping("/get/by/type/week/{customerId}")
    public List<WorkoutDto> getWorkoutsByTypeAndWeek(@RequestParam String type, @RequestParam String week, @PathVariable Long customerId) {
        return workoutService.getWorkoutsByTypeAndWeek(type, week, customerId);
    }

    @PostMapping("save/customer/{id}")
    public ResponseEntity<String> saveWorkoutWithCustomer(@RequestBody List<WorkoutDto> workoutsDto, @PathVariable Long id) {
    workoutService.saveWorkoutWithCustomer(workoutsDto, id);
    return ResponseEntity.ok("Workout Saved");
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

    @DeleteMapping("/delete/{workoutId}/customer/{customerId}")
    public ResponseEntity<Void> deleteWorkoutWithCustomer(
            @PathVariable Long workoutId,
            @PathVariable Long customerId) {

        try {
            workoutService.deleteWorkoutCustomer(workoutId, customerId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("delete/by/week/{customerId}")
    public ResponseEntity<Void> deleteWorkoutByWeek(@RequestParam String week, @PathVariable Long customerId) {
        workoutService.deleteWorkoutByWeek(week, customerId);
        try {
        return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/customer/{id}")
    public List<WorkoutDto> getWorkoutUserById(@PathVariable Long id) {
       return workoutService.workoutOfCustomer(id);
    }
}