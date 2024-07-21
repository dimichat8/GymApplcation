package com.gym.app.workout.repository;

import com.gym.app.customer.entity.Customer;
import com.gym.app.dto.WorkoutDto;
import com.gym.app.workout.entity.Workout;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    @Query("select count(w) from Workout w")
    int countWorkouts();

    @Query("select w from Workout w where w.customer =:customer")
    List<Workout> findWorkoutsByCustomer(@Param("customer") Optional<Customer> customer);

    @Transactional
    @Modifying
    @Query("delete from Workout w where w.id = :workoutId and w.customer = :customer")
    void deleteWorkoutsByCustomer(@Param("workoutId") Long workoutId, @Param("customer") Customer customer);

    @Transactional
    @Modifying
    @Query("delete from Workout w where w.customer = :customer and w.week = :week")
    void deleteWorkoutsByWeek(String week, @Param("customer") Customer customer);

    @Query("select w from Workout w where w.type =:type and w.week =:week and w.customer = :customer")
    List<Workout> findWorkoutByTypeAndWeek(String type, String week, Optional<Customer> customer);
}
