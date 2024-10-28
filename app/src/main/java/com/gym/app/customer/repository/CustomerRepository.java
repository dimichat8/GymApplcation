package com.gym.app.customer.repository;

import com.gym.app.contactInfo.entity.ContactInfo;
import com.gym.app.customer.entity.GymCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<GymCustomer, Long> {

    @Query("select c from GymCustomer c where c.contactInfo.email = :email")
    GymCustomer findByEmail(@Param("email") String email);

    @Query("SELECT c FROM GymCustomer c  WHERE c.firstname=:firstname and c.surname=:surname and c.age=:age")
    GymCustomer findCustomerByName(String firstname, String surname, LocalDate age);

    @Query("select c from ContactInfo c where c.customer=:customer")
    ContactInfo findContactInfoByCustomer(Optional<GymCustomer> customer);

    @Query("select count(c) from GymCustomer c join c.user u join u.contactInfo con where c.isEnabled = true and con.email = :email")
    int countActiveCustomers(@Param("email") String email);

    @Query("select count(c) from GymCustomer c join c.user u join u.contactInfo con where c.isEnabled = false and con.email = :email")
    int countDisabledCustomers(@Param("email") String email);

    @Query("select count(w.id) from GymCustomer c" +
            " join c.user u " +
            " join u.contactInfo con " +
            " left join c.workouts w " +
            "   where con.email = :email ")
    int countWorkouts(@Param("email") String email);

    @Query("select c from GymCustomer c where c.firstname=:firstname and c.surname=:surname")
    Optional<GymCustomer> findCustomerByFirstnameAndSurname(@Param("firstname") String firstname, @Param("surname") String surname);

    @Query("select c from GymCustomer c where c.contactInfo.email = :email")
    Optional<GymCustomer> findOptionalCustomerByEmail(String email);

    @Query("select c from GymCustomer c where c.contactInfo.email = :email and c.contactInfo.mobilePhone = :phone")
    Optional<GymCustomer> findCustomerByEmailAndPhone(String email, String phone);

    @Query("select c from GymCustomer c join c.user u join u.contactInfo con where con.email = :email")
    List<GymCustomer> getCustomersByUser(@Param("email") String email);
}
