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

import java.util.ArrayList;
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
        public List<WorkoutDto> getWorkoutsByTypeAndWeek(String type, String week, Long customerId) {
            Optional<Customer> customer = customerRepository.findById(customerId);
            List<WorkoutDto> workoutDtos = new ArrayList<>();
            if (customer.isPresent()) {
                List<Workout> workoutList = workoutRepository.findWorkoutByTypeAndWeek(type, week, customer);
                workoutDtos = workoutList.stream().map(Map::covertToWorkoutDto).collect(Collectors.toList());
            }
            return workoutDtos;
        }

        @Override
        public void saveWorkoutWithCustomer(List<WorkoutDto> workoutsDto, Long id) {
            Optional<Customer> optCustomer = customerRepository.findById(id);
            Customer customer;
            if (optCustomer.isPresent()) {
                customer = optCustomer.get();
                customerRepository.save(customer);
            } else {
                customer = null;
            }
            List<Workout> workouts = Map.mapToWorkouts(workoutsDto);
            workouts.forEach(workout -> workout.setCustomer(customer));
            workoutRepository.saveAll(workouts);
        }

        @Override
        public Workout saveWorkout(WorkoutDto workoutDto) {
            Workout workout = new Workout();
            workout.setName(workoutDto.getName());
            workout.setType(workoutDto.getType());
            workout.setDuration(workoutDto.getDuration());
            workout.setWeek(workoutDto.getWeek());
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

        @Override
        public void deleteWorkoutCustomer(Long id) {
            Optional<Customer> customer = customerRepository.findById(id);
            workoutRepository.deleteWorkoutsByCustomer(customer);
        }

        @Override
        public List<WorkoutDto> workoutOfCustomer(Long id) {
            Optional<Customer> customer = customerRepository.findById(id);
            List<WorkoutDto> workoutDtos = new ArrayList<>();
            if (customer.isPresent()) {
                List<Workout> workouts = workoutRepository.findWorkoutsByCustomer(customer);
                workoutDtos = workouts.stream().map(Map::covertToWorkoutDto).toList();
            }
            return workoutDtos;
        }
}

