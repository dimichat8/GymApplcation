package com.gym.app.customer.service.CustomerServiceImpl;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.contactInfo.repository.ContactInfoRepository;
import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.customer.service.CustomerService;
import com.gym.app.dto.CustomerDto;
import com.gym.app.mapper.Map;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

                    // Επιστροφή του ονόματος του αρχείου
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
            try {
                String fileName = customerOptional.get().getFirstname() + "_" + customerOptional.get().getAge() + "_" + customerOptional.get().getSurname() + ".jpeg";
                String filePath = Paths.get(destDir, fileName).toString();
                String nameOfPicture = Paths.get(fileName).toString();
                File dest = new File(filePath);
                profilePicture.transferTo(dest);


                customer.setProfilePicture(nameOfPicture.getBytes());
                customerRepository.save(customer);
                System.out.println(customer);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            customer.setAge(customerDto.getAge());

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
}
