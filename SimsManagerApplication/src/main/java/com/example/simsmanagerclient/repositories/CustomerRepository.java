package com.example.simsmanagerclient.repositories;

import com.example.simsmanagerclient.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
