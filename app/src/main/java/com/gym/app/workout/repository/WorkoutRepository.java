package com.gym.app.workout.repository;

import com.gym.app.workout.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    @Query("select count(w) from Workout w")
    int countWorkouts();
}
