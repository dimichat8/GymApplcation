package com.gym.app.customer.repository;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.contactInfo.email = :email")
    Customer findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c  WHERE c.firstname=:firstname and c.surname=:surname and c.age=:age")
    Customer findCustomerByName(String firstname, String surname, LocalDate age);

    @Query("select c from ContactInfo c where c.customer=:customer")
    ContactInfo findContactInfoByCustomer(Optional<Customer> customer);

    @Query("select count(c) from Customer  c where c.isEnabled = true")
    int countActiveCustomers();

    @Query("select count(c) from Customer  c where c.isEnabled = false")
    int countDisabledCustomers();

    @Query("select c from Customer c where c.firstname=:firstname and c.surname=:surname")
    Optional<Customer> findCustomerByFirstnameAndSurname(@Param("firstname") String firstname, @Param("surname") String surname);

    @Query("select c from Customer c where c.contactInfo.email = :email")
    Optional<Customer> findOptionalCustomerByEmail(String email);

    @Query("select c from Customer c where c.contactInfo.email = :email and c.contactInfo.mobilePhone = :phone")
    Optional<Customer> findCustomerByEmailAndPhone(String email, String phone);
}
