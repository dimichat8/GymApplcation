package com.gym.app.workout.service.impl;

import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.dto.WorkoutDto;
import com.gym.app.mapper.Map;
import com.gym.app.security.authentication.UserInfoDetailsService;
import com.gym.app.user.repository.UserRepository;
import com.gym.app.workout.entity.Workout;
import com.gym.app.workout.repository.WorkoutRepository;
import com.gym.app.workout.service.WorkoutService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        @Autowired
        private UserRepository userRepository;

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
    public void deleteWorkoutCustomer(Long workoutId, Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        Optional<Workout> workout = workoutRepository.findById(workoutId);

        if (customer.isPresent() && workout.isPresent()) {
            workoutRepository.deleteWorkoutsByCustomer(workout.get().getId(), customer.get());
        } else if (workout.isPresent()) {
            throw new RuntimeException("Customer not found");
        } else if (customer.isPresent()) {
            throw new RuntimeException("Workout not found");
        } else {
            throw new RuntimeException("Customer and Workout not found");
        }
    }
    @Override
    public void deleteWorkoutByWeek(String week, Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            workoutRepository.deleteWorkoutsByWeek(week, customer.get());
        } else {
            throw new RuntimeException("Customer and Workout not found");
        }
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

    @Transactional
    @Override
    public List<WorkoutDto> myProgramme() {
        List<WorkoutDto> workoutDtos;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object userDetails = authentication.getPrincipal();
                if (userDetails != null) {
                    Optional<Customer> customer = Optional.ofNullable(customerRepository.findOptionalCustomerByEmail(((UserInfoDetailsService) userDetails).getUsername())
                            .orElseThrow(() -> new UsernameNotFoundException("User not found")));;
                    if (customer.isPresent()) {
                        List<Workout> workouts = workoutRepository.myWorkouts(customer.get().getFirstname(), customer.get().getSurname());
                        workoutDtos = workouts.stream().map(Map::covertToWorkoutDto).toList();
                        return workoutDtos;
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @Transactional
    @Override
    public List<WorkoutDto> getWorkoutsForAthlete(String type, String week) {
            List<WorkoutDto> workoutDtos;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object userDetails = authentication.getPrincipal();
            if (userDetails != null) {
                Optional<Customer> customer = Optional.ofNullable(customerRepository.findOptionalCustomerByEmail(((UserInfoDetailsService) userDetails).getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));;
                if (customer.isPresent()) {
                    List<Workout> workouts = workoutRepository.myWorkouts(customer.get().getFirstname(), customer.get().getSurname());
                    workoutDtos = workouts.stream().map(Map::covertToWorkoutDto).toList();
                    return workoutDtos;
                }
            }
        }
        return List.of();
    }
}

