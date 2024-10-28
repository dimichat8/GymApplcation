package com.gym.app.customer.service;

import com.gym.app.customer.entity.GymCustomer;
import com.gym.app.dto.CustomerGymDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerGymDto> getCustomers();

    Optional<CustomerGymDto> getCustomerById(Long id);

    String getPicture(Long id);

    Long registerCustomer(CustomerGymDto customerDto);

    void uploadProfilePicture(Long customerId, MultipartFile profilePicture);

    void updateCustomer(CustomerGymDto customerDto, Long id);

    void deleteCustomer(@PathVariable Long id);

    Optional<GymCustomer> getCustomer(String firstname, String surname);

    List<CustomerGymDto> getAllCustomersByUser(String email);
}
