package com.gym.app.contactInfo.repository;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    @Query("select c from ContactInfo c where c.user=:user")
    ContactInfo findContactInfoByUser(Optional<User> user);
}
