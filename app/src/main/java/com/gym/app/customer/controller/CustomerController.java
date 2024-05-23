package com.gym.app.customer.controller;

import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.service.CustomerService;
import com.gym.app.dto.CustomerDto;
import com.gym.app.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    @GetMapping("/getCustomers")
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customerList = customerService.getCustomers();
        return ResponseEntity.ok(customerList);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CustomerDto customerDto) {
        customerService.registerCustomer(customerDto);
        return ResponseEntity.ok("Customer '" + customerDto.getSurname() + "' registered successfully!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@RequestBody CustomerDto customerDto, @PathVariable Long id) {
        customerService.updateCustomer(customerDto, id);
        return ResponseEntity.ok("Customer updated successfully!");
    }
}
