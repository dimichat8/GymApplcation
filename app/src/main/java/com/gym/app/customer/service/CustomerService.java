package com.gym.app.customer.service;

import com.gym.app.dto.CustomerDto;
import com.gym.app.dto.UserDto;
import com.gym.app.user.entity.User;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerDto> getCustomers();

    Optional<CustomerDto> getCustomerById(Long id);

    void registerCustomer(CustomerDto customerDto);

    void updateCustomer(CustomerDto customerDto, Long id);

    void deleteCustomer(@PathVariable Long id);
}
