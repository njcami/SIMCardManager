package com.example.simsmanagerclient.repositories;

import com.example.simsmanagerclient.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
