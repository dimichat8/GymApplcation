package com.gym.app.mapper;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.dto.ContactInfoDto;
import com.gym.app.dto.CustomerDto;
import com.gym.app.dto.UserDto;
import com.gym.app.dto.WorkoutDto;
import com.gym.app.user.entity.User;
import com.gym.app.workout.entity.Workout;
import org.springframework.beans.factory.annotation.Autowired;

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

    public static CustomerDto convertToCustomerDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setFirstname(customer.getFirstname());
        customerDto.setSurname(customer.getSurname());
        customerDto.setGender(customer.getGender());
        customerDto.setAge(customer.getAge());
        customerDto.setIsEnabled(customer.getIsEnabled());
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

    public static UserDto convertToUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
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
                Customer customer = user.getCustomerList().get(0); // Assuming you want the first customer
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

    public static User convertToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
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
        return workout;
    }

    public static WorkoutDto covertToWorkoutDto(Workout workout) {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setId(workout.getId());
        workoutDto.setName(workout.getName());
        workoutDto.setType(workout.getType());
        workoutDto.setDuration(workout.getDuration());
        return workoutDto;
    }

}
