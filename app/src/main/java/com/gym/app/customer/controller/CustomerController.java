package com.gym.app.customer.controller;

import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.service.CustomerService;
import com.gym.app.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
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
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerDto customerDto) {
        customerService.registerCustomer(customerDto);
        return ResponseEntity.ok("Customer '" + customerDto.getSurname() + "' registered successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCustomerPicture(@PathVariable Long id) {
       String picture = customerService.getPicture(id);

        if (picture != null) {
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{customerId}/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(
            @PathVariable Long customerId,
            @RequestParam("profilePicture") MultipartFile profilePicture) {
        customerService.uploadProfilePicture(customerId, profilePicture);
        return ResponseEntity.ok("Profile picture uploaded successfully!");
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerDto customerDto, @PathVariable Long id) {
        customerService.updateCustomer(customerDto, id);
        return ResponseEntity.ok("Customer updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted successfully!");
    }

    @GetMapping("get/name")
    public ResponseEntity<String> getCustomerName(@RequestParam String firstname, @RequestParam String surname) {
        Optional<Customer> customer = customerService.getCustomer(firstname, surname);
        if (customer.isPresent()) {
            return ResponseEntity.ok("Customer name is: " + customer.get().getFirstname() + " " + customer.get().getSurname());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
