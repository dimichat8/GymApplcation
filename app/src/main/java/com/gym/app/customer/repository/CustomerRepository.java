package com.gym.app.customer.repository;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.customer.entity.Customer;
import com.gym.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c  WHERE c.firstname=:firstname and c.surname=:surname and c.age=:age")
    Customer findCustomerByName(String firstname, String surname, LocalDate age);

    @Query("select c from ContactInfo c where c.customer=:customer")
    ContactInfo findContactInfoByCustomer(Optional<Customer> customer);
}
