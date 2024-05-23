package com.gym.app.customer.service.CustomerServiceImpl;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.contactInfo.repository.ContactInfoRepository;
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
    private ContactInfoRepository contactInfoRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void registerCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
            customer.setId(customerDto.getId());
            customer.setFirstname(customerDto.getFirstname());
            customer.setSurname(customerDto.getSurname());
            customer.setGender(customerDto.getGender());
            if (customerDto.getIsEnabled() == null) {
                customerDto.setIsEnabled(true);
            }
            customer.setIsEnabled(customerDto.getIsEnabled());
            customer.setAge(customerDto.getAge());

            Customer savedCustomer = customerRepository.save(customer);
            ContactInfo contactInfo = new ContactInfo();
                contactInfo.setPhone(customerDto.getContactInfoDto().getPhone());
                contactInfo.setEmail(customerDto.getContactInfoDto().getEmail());
                contactInfo.setMobilePhone(customerDto.getContactInfoDto().getMobilePhone());
            customer.setContactInfo(contactInfo);
            contactInfo.setCustomer(savedCustomer);

            if (customerDto.getUserId() != null) {
                Optional<User> user = userRepository.findById(customerDto.getUserId());
                if (user.isPresent()) {
                    customer.setUser(user.get());
                }
            }
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(CustomerDto customerDto, Long id) {
        Optional<Customer> existingCustomerOpt = customerRepository.findById(id);
        if (existingCustomerOpt.isPresent()) {
            Customer customer = existingCustomerOpt.get();
            customer.setFirstname(customerDto.getFirstname());
            customer.setSurname(customerDto.getSurname());
            customer.setIsEnabled(customerDto.getIsEnabled());
            customer.setAge(customerDto.getAge());
            customer.setAge(customerDto.getAge());


            ContactInfo contactInfo = customerRepository.findContactInfoByCustomer(existingCustomerOpt);
            contactInfo.setPhone(customerDto.getContactInfoDto().getPhone());
            contactInfo.setEmail(customerDto.getContactInfoDto().getEmail());
            contactInfo.setMobilePhone(customerDto.getContactInfoDto().getMobilePhone());
            contactInfo.setCustomer(customer);
            contactInfoRepository.save(contactInfo);
            customer.setContactInfo(contactInfo);
            customerRepository.save(customer);
        }
    }
}
