package com.gym.app.user.service.UserServiceImpl;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.contactInfo.repository.ContactInfoRepository;
import com.gym.app.dto.UserDto;
import com.gym.app.user.entity.User;
import com.gym.app.user.repository.UserRepository;
import com.gym.app.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void registerUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(/*passwordEncoder.encode(*/userDto.getPassword());
        user.setIsLoggedIn(userDto.getIsLoggedIn());
        user.setRole(userDto.getRole());
        user.setCustomerList(userDto.getCustomerList());

        User savedUser = userRepository.save(user);
        ContactInfo contactInfo = new ContactInfo();
            contactInfo.setPhone(userDto.getContactInfoDto().getPhone());
            contactInfo.setEmail(userDto.getContactInfoDto().getEmail());
            contactInfo.setMobilePhone(userDto.getContactInfoDto().getMobilePhone());
            contactInfo.setUser(savedUser);
        user.setContactInfo(contactInfo);
        contactInfoRepository.save(contactInfo);
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDto userDto, Long id) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User user = existingUserOpt.get();
            user.setName(userDto.getName());
            user.setPassword(userDto.getPassword());
            user.setIsLoggedIn(userDto.getIsLoggedIn());
            user.setRole(userDto.getRole());

            ContactInfo contactInfo = contactInfoRepository.findContactInfoByUser(existingUserOpt);
                        contactInfo.setPhone(userDto.getContactInfoDto().getPhone());
                        contactInfo.setEmail(userDto.getContactInfoDto().getEmail());
                        contactInfo.setMobilePhone(userDto.getContactInfoDto().getMobilePhone());
                        contactInfo.setUser(user);
                        contactInfoRepository.save(contactInfo);
            user.setContactInfo(contactInfo);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            userRepository.deleteById(id);}
    }
}