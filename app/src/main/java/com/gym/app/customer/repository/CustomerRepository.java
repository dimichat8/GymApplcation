package com.gym.app.customer.repository;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.contactInfo.email = :email")
    Customer findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c  WHERE c.firstname=:firstname and c.surname=:surname and c.age=:age")
    Customer findCustomerByName(String firstname, String surname, LocalDate age);

    @Query("select c from ContactInfo c where c.customer=:customer")
    ContactInfo findContactInfoByCustomer(Optional<Customer> customer);

    @Query("select count(c) from Customer c join c.user u join u.contactInfo con where c.isEnabled = true and con.email = :email")
    int countActiveCustomers(@Param("email") String email);

    @Query("select count(c) from Customer c join c.user u join u.contactInfo con where c.isEnabled = false and con.email = :email")
    int countDisabledCustomers(@Param("email") String email);

    @Query("select count(w.id) from Customer c" +
            " join c.user u " +
            " join u.contactInfo con " +
            " left join c.workouts w " +
            "   where con.email = :email ")
    int countWorkouts(@Param("email") String email);

    @Query("select c from Customer c where c.firstname=:firstname and c.surname=:surname")
    Optional<Customer> findCustomerByFirstnameAndSurname(@Param("firstname") String firstname, @Param("surname") String surname);

    @Query("select c from Customer c where c.contactInfo.email = :email")
    Optional<Customer> findOptionalCustomerByEmail(String email);

    @Query("select c from Customer c where c.contactInfo.email = :email and c.contactInfo.mobilePhone = :phone")
    Optional<Customer> findCustomerByEmailAndPhone(String email, String phone);

    @Query("select c from Customer c join c.user u join u.contactInfo con where con.email = :email")
    List<Customer> getCustomersByUser(@Param("email") String email);
}
