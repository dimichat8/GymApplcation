package com.gym.app.mapper;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.customer.entity.GymCustomer;
import com.gym.app.dto.ContactInfoDto;
import com.gym.app.dto.CustomerGymDto;
import com.gym.app.dto.UserGymDto;
import com.gym.app.dto.WorkoutDto;
import com.gym.app.user.entity.User;
import com.gym.app.workout.entity.Workout;

import java.util.List;
import java.util.stream.Collectors;

public class Map {

    public static ContactInfo toContactInfo(ContactInfoDto dto) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setMobilePhone(dto.getMobilePhone());
        contactInfo.setEmail(dto.getEmail());
        return contactInfo;
    }

    public static List<ContactInfo> toContactInfoList(List<ContactInfoDto> dtoList) {
        return dtoList.stream()
                .map(Map::toContactInfo)
                .collect(Collectors.toList());
    }

    public static CustomerGymDto convertToCustomerDto(GymCustomer customer) {
        if (customer == null) {
            return null;
        }
        CustomerGymDto customerDto = new CustomerGymDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstname(customer.getFirstname());
        customerDto.setSurname(customer.getSurname());
        customerDto.setGender(customer.getGender());
        customerDto.setAge(customer.getAge());
        customerDto.setIsEnabled(customer.getIsEnabled());
        customerDto.setProfilePictureName(customer.getProfilePictureName());
        customerDto.setProfilePicture(customer.getProfilePicture());
        if (customer.getContactInfo() != null) {
            ContactInfoDto contactInfoDto = new ContactInfoDto();
            contactInfoDto.setId(customer.getContactInfo().getId());
            contactInfoDto.setPhone(customer.getContactInfo().getPhone());
            contactInfoDto.setEmail(customer.getContactInfo().getEmail());
            contactInfoDto.setMobilePhone(customer.getContactInfo().getMobilePhone());
            if (customer.getUser() != null) {
                contactInfoDto.setUserId(customer.getUser().getId());
            }
            contactInfoDto.setCustomerId(customer.getId());
            customerDto.setContactInfoDto(contactInfoDto);
        }
        if (customer.getUser() != null) {
            customerDto.setUserId(customer.getUser().getId());
        }

        return customerDto;
    }

    /*public static Customer convertToCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        customer.setFirstname(customerDto.getFirstname());
        customer.setSurname(customerDto.getSurname());
        customer.setGender(customerDto.getGender());
        customer.setAge(customerDto.getAge());
        customer.setIsEnabled(customerDto.getIsEnabled());
        if (customerDto.getContactInfoDto() != null) {
            ContactInfoDto contactInfoDto = new ContactInfoDto();
            contactInfoDto.setId(customerDto.getContactInfoDto().getId());
            contactInfoDto.setPhone(customerDto.getContactInfoDto().getPhone());
            contactInfoDto.setEmail(customerDto.getContactInfoDto().getEmail());
            contactInfoDto.setMobilePhone(customerDto.getContactInfoDto().getMobilePhone());
        }
        if (customerDto.getUserId() != null){
            //customer.setUser(convertToUser(customerDto.getUserId()));
        }
        return customer;
    }*/

    public static UserGymDto convertToUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserGymDto userDto = new UserGymDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setIsLoggedIn(user.getIsLoggedIn());
        userDto.setRole(user.getRole());
        if (user.getContactInfo() != null) {
            ContactInfoDto contactInfoDto = new ContactInfoDto();
            contactInfoDto.setId(user.getContactInfo().getId());
            contactInfoDto.setPhone(user.getContactInfo().getPhone());
            contactInfoDto.setEmail(user.getContactInfo().getEmail());
            contactInfoDto.setMobilePhone(user.getContactInfo().getMobilePhone());
            contactInfoDto.setUserId(user.getId());
            if (!user.getCustomerList().isEmpty()) {
                GymCustomer customer = user.getCustomerList().get(0);
                contactInfoDto.setCustomerId(customer.getId());
            }
            userDto.setContactInfoDto(contactInfoDto);
        }
       /* if (user.getCustomerList() != null) {
            userDto.setCustomerList(user.getCustomerList().stream()
                    .map(Map::convertToCustomerDto)
                    .collect(Collectors.toList()));
        }*/

        return userDto;
    }

    public static User convertToUser(UserGymDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setIsLoggedIn(userDto.getIsLoggedIn());
        user.setRole(userDto.getRole());
        if (userDto.getContactInfoDto() != null) {
            ContactInfoDto contactInfoDto = new ContactInfoDto();
            contactInfoDto.setId(userDto.getContactInfoDto().getId());
            contactInfoDto.setPhone(userDto.getContactInfoDto().getPhone());
            contactInfoDto.setEmail(userDto.getContactInfoDto().getEmail());
            contactInfoDto.setMobilePhone(userDto.getContactInfoDto().getMobilePhone());
            contactInfoDto.setUserId(userDto.getId());
            //contactInfoDto.setCustomerId(userDto.getId());
        }
        return user;
    }

    public static Workout covertToWorkout(WorkoutDto workoutDto) {
        if (workoutDto == null) {
            return null;
        }
        Workout workout = new Workout();
        workout.setId(workoutDto.getId());
        workout.setName(workoutDto.getName());
        workout.setType(workoutDto.getType());
        workout.setDuration(workoutDto.getDuration());
        workout.setDescription(workoutDto.getDescription());
        workout.setWeek(workoutDto.getWeek());
        return workout;
    }

    public static WorkoutDto covertToWorkoutDto(Workout workout) {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setId(workout.getId());
        workoutDto.setName(workout.getName());
        workoutDto.setType(workout.getType());
        workoutDto.setDuration(workout.getDuration());
        workoutDto.setDescription(workout.getDescription());
        workoutDto.setWeek(workout.getWeek());
        return workoutDto;
    }

        public static List<Workout> mapToWorkouts(List<WorkoutDto> workoutDtos) {
            return workoutDtos.stream().map(workoutDto -> {
                Workout workout = new Workout();
                workout.setName(workoutDto.getName());
                workout.setType(workoutDto.getType());
                workout.setDuration(workoutDto.getDuration());
                workout.setDescription(workoutDto.getDescription());
                workout.setWeek(workoutDto.getWeek());
                return workout;
            }).collect(Collectors.toList());
    }

}
