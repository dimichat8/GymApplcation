package com.gym.app.customer.controller;

import com.gym.app.customer.service.CustomerService;
import com.gym.app.dto.CustomerDto;
import com.gym.app.dto.UserDto;
import com.gym.app.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {


    @Autowired
    private CustomerService customerService;

    @GetMapping("/getCustomers")
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        List<CustomerDto> customerList = customerService.getCustomers();
        return ResponseEntity.ok(customerList);
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<Optional<CustomerDto>> getCustomerById(@PathVariable Long id) {
        Optional<CustomerDto> user = customerService.getCustomerById(id);
        return ResponseEntity.ok(user);
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted successfully!");
    }
}
