package com.gym.app.customer.service;

import com.gym.app.dto.CustomerDto;
import com.gym.app.dto.UserDto;
import com.gym.app.user.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerDto> getCustomers();

    Optional<CustomerDto> getCustomerById(Long id);

    String getPicture(Long id);

    Long registerCustomer(CustomerDto customerDto);

    void uploadProfilePicture(Long customerId, MultipartFile profilePicture);

    void updateCustomer(CustomerDto customerDto, Long id);

    void deleteCustomer(@PathVariable Long id);
}
