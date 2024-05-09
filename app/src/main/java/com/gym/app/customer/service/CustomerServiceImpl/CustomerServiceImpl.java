package com.gym.app.customer.service.CustomerServiceImpl;

import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.customer.service.CustomerService;
import com.gym.app.dto.CustomerDto;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void registerUser(CustomerDto customerDto) {
        Customer customer = new Customer();
            customer.setId(customerDto.getId());
            customer.setFirstname(customerDto.getFirstname());
            customer.setSurname(customerDto.getSurname());
            customer.setGender(customerDto.getGender());
            customer.setAge(customerDto.getAge());
            if (customerDto.getUser_id() != null) {
                Optional<User> user = userRepository.findById(customerDto.getUser_id());
                customer.setUser(user.get());
            }
        customerRepository.save(customer);
    }
}
