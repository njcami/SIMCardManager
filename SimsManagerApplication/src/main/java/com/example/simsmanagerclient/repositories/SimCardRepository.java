package com.example.simsmanagerclient.repositories;

import com.example.simsmanagerclient.entities.SimCard;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Min;
import java.util.List;

public interface SimCardRepository extends JpaRepository<SimCard, Long> {

    public List<SimCard> findByCustomerId(final @Min(1L) Long customerId);
}
