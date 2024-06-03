package com.gym.app.workout.service.impl;

import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.dto.WorkoutDto;
import com.gym.app.mapper.Map;
import com.gym.app.workout.entity.Workout;
import com.gym.app.workout.repository.WorkoutRepository;
import com.gym.app.workout.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkoutServiceImpl implements WorkoutService {

        @Autowired
        private WorkoutRepository workoutRepository;
        @Autowired
        private CustomerRepository customerRepository;

        @Override
        public List<WorkoutDto> getAllWorkouts() {
            return workoutRepository.findAll().stream()
                    .map(Map::covertToWorkoutDto)
                    .collect(Collectors.toList());
        }

        @Override
        public Workout getWorkoutById(Long id) {
            return workoutRepository.findById(id).orElse(null);
        }

        @Override
        public Workout saveWorkout(WorkoutDto workoutDto) {
            Workout workout = new Workout();
            workout.setName(workoutDto.getName());
            workout.setType(workoutDto.getType());
            workout.setDuration(workoutDto.getDuration());
            if (workoutDto.getCustomerId() != null) {
                Optional<Customer> optionalWorkout = customerRepository.findById(workoutDto.getCustomerId());
                workout.setCustomer(optionalWorkout.get());
            }
            return workoutRepository.save(workout);
        }

        @Override
        public void deleteWorkout(Long id) {
            workoutRepository.deleteById(id);
        }
}

