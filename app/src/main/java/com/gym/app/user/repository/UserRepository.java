package com.gym.app.user.repository;
import com.gym.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select count(*) from User")
    int countUsers();

    @Query("select u from User u where u.contactInfo.email = :email")
    User findUserByEmail(@Param("email") String email);

    @Query("select u from User u where u.contactInfo.email = :email")
    Optional<User> findOptionalUserByEmail(@Param("email") String email);

    @Query("select u from User u where u.contactInfo.email = :email and u.contactInfo.mobilePhone = :phone")
    Optional<User> findUserByEmailAndPhone(String email, String phone);
}