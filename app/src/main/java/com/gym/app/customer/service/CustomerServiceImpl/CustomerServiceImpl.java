package com.gym.app.customer.service.CustomerServiceImpl;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.contactInfo.repository.ContactInfoRepository;
import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.customer.service.CustomerService;
import com.gym.app.dto.CustomerDto;
import com.gym.app.mapper.Map;
import com.gym.app.security.authentication.UserInfoDetailsService;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.dest-dir}")
    private String destDir;


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ContactInfoRepository contactInfoRepository;
    @Autowired
    private UserRepository userRepository;

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public List<CustomerDto> getCustomers() {
        return customerRepository.findAll().stream()
                .map(Map::convertToCustomerDto)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<CustomerDto> getCustomerById(Long id) {
        return customerRepository.findById(id).map(Map::convertToCustomerDto);
    }

    @Override
    public Long registerCustomer(CustomerDto customerDto) {
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
            Long customerId = savedCustomer.getId();
            System.out.println(customerId);
            ContactInfo contactInfo = new ContactInfo();
            if (customerDto.getContactInfoDto() != null) {
                contactInfo.setPhone(customerDto.getContactInfoDto().getPhone());
                contactInfo.setEmail(customerDto.getContactInfoDto().getEmail());
                contactInfo.setMobilePhone(customerDto.getContactInfoDto().getMobilePhone());
                customer.setContactInfo(contactInfo);
                contactInfo.setCustomer(savedCustomer);
            }

        if (customerDto.getUserId() != null) {
            Optional<User> user = userRepository.findById(customerDto.getUserId());
            user.ifPresent(customer::setUser);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object userDetails = authentication.getPrincipal();
                if (userDetails != null) {
                    Optional<User> user = userRepository.findOptionalUserByEmail(((UserInfoDetailsService) userDetails).getUsername());
                    customer.setUser(user.get());
                }
            }
        customerRepository.save(customer);
        return customerId;
    }

    @Override
    public String getPicture(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (customerOptional.isPresent()) {
            byte[] pictureBytes = customerOptional.get().getProfilePicture();
            if (pictureBytes != null && pictureBytes.length > 0) {
                try {
                    // Δημιουργία προσωρινού αρχείου για να αποκτήσετε το όνομα του αρχείου
                    File tempFile = File.createTempFile(customerOptional.get().getFirstname() + "_" + customerOptional.get().getFirstname(), ".jpg");
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(pictureBytes);
                    fos.close();

                    String fileName = tempFile.getName();
                    tempFile.delete(); // Διαγραφή του προσωρινού αρχείου
                    return fileName;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void uploadProfilePicture(Long customerId, MultipartFile profilePicture) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();

            String contentType = profilePicture.getContentType();
            String extension = "";
            if (contentType != null) {
                switch (contentType) {
                    case "image/jpeg":
                        extension = "jpeg";
                        break;
                    case "image/png":
                        extension = "png";
                        break;
                    case "image/gif":
                        extension = "gif";
                        break;
                    // Add more cases if needed
                    default:
                        throw new IllegalArgumentException("Unsupported file type: " + contentType);
                }
            }

            // Construct file name and path
            String fileName = customer.getFirstname() + "_" + customer.getAge() + "_" + customer.getSurname() + "." + extension;
            String filePath = Paths.get(destDir, fileName).toString();

            // Save the file
            File dest = new File(filePath);
            try {
                profilePicture.transferTo(dest);

                // Update the customer's profile picture path in the database
                customer.setProfilePicture(fileName.getBytes());
                customer.setProfilePictureName(fileName);
                customerRepository.save(customer);
                System.out.println("Profile picture uploaded successfully: " + customer);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload file: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Customer not found with id: " + customerId);
        }
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
            customer.setGender(customerDto.getGender());

            ContactInfo contactInfo = customerRepository.findContactInfoByCustomer(existingCustomerOpt);
            if (customerDto.getContactInfoDto() != null && contactInfo != null) {
                contactInfo.setPhone(customerDto.getContactInfoDto().getPhone());
                contactInfo.setEmail(customerDto.getContactInfoDto().getEmail());
                contactInfo.setMobilePhone(customerDto.getContactInfoDto().getMobilePhone());
                contactInfo.setCustomer(customer);
                contactInfoRepository.save(contactInfo);
                customer.setContactInfo(contactInfo);
            }
            customerRepository.save(customer);
        }
    }

   @Override
    public void deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        customerOpt.ifPresent(customer -> customerRepository.delete(customer));
    }

    @Override
    public Optional<Customer> getCustomer(String firstname, String surname) {
        Optional<Customer> customer = customerRepository.findCustomerByFirstnameAndSurname(firstname, surname);
        if (customer.isPresent()) {
            logger.info("Customer name is: " + customer.get().getFirstname() + " " + customer.get().getSurname());
        }
        return customerRepository.findCustomerByFirstnameAndSurname(firstname, surname);
    }
}
