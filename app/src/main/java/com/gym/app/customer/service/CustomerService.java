package com.gym.app.customer.service;

import com.gym.app.customer.entity.Customer;
import com.gym.app.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    List<Customer> getCustomers();

    void registerCustomer(CustomerDto customerDto);

    void updateCustomer(CustomerDto customerDto, Long id);
}
