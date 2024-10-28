package com.gym.app.customer.controller;

import com.gym.app.customer.entity.GymCustomer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.customer.service.CustomerService;
import com.gym.app.dto.CustomerGymDto;
import com.gym.app.enums.Role;
import com.gym.app.security.authentication.UserInfoDetailsService;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import com.gym.app.workout.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getCustomer/by/login")
    public ResponseEntity<Long> getCustomerByLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object userDetails = authentication.getPrincipal();
            Object authority = authentication.getAuthorities();
            if (userDetails != null) {
                String email = ((UserInfoDetailsService) userDetails).getUsername();
                Optional<User> user = Optional.empty();
                Optional<GymCustomer> customer = Optional.empty();
                List<Object> o = new ArrayList<>();
                if (authority.equals("ROLE_" + Role.ATHLETE)) {
                    user = Optional.of(Optional.ofNullable(userRepository.findUserByEmail(email))
                            .orElseThrow(() -> new UsernameNotFoundException("User not found")));
                    o.add(user);
                } else {
                    customer = Optional.ofNullable(customerRepository.findOptionalCustomerByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("Athlete not found")));
                    o.add(customer);
                }
                Object object = o.get(0);
                if (((Optional<?>) object).get() instanceof User) {
                    return ResponseEntity.ok(user.get().getId());
                } else if (((Optional<?>) object).get() instanceof GymCustomer) {
                    return ResponseEntity.ok(customer.get().getId());
                } else  {
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return null;
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<List<CustomerGymDto>> getCustomers() {
        List<CustomerGymDto> customerList = customerService.getCustomers();
        return ResponseEntity.ok(customerList);
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<Optional<CustomerGymDto>> getCustomerById(@PathVariable Long id) {
        Optional<CustomerGymDto> user = customerService.getCustomerById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody CustomerGymDto customerDto) {
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
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerGymDto customerDto, @PathVariable Long id) {
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
        Optional<GymCustomer> customer = customerService.getCustomer(firstname, surname);
        if (customer.isPresent()) {
            return ResponseEntity.ok("Customer name is: " + customer.get().getFirstname() + " " + customer.get().getSurname());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getCustomerByUser")
    public ResponseEntity<List<CustomerGymDto>> getCustomerByUser(@RequestParam String email) {
        List<CustomerGymDto> customerDtoList = customerService.getAllCustomersByUser(email);
        return ResponseEntity.ok(customerDtoList);
    }
}
