package com.example.application.repo;

import java.util.List;


import com.example.application.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
}
