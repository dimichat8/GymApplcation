package com.gym.app.customer.repository;

import com.gym.app.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c  WHERE c.firstname=:firstname and c.surname=:surname and c.age=:age")
    Customer findCustomerByName(String firstname, String surname, LocalDate age);
}
