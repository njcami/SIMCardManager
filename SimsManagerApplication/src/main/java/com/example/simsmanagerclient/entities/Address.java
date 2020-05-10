package com.example.simsmanagerclient.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_number")
    private Integer houseNumber;

    @Column(name = "house_name", length = 50)
    private String houseName;

    @Column(length = 150)
    private String streetName;

    @Column(length = 50)
    private String locality;

    @Column(length = 30)
    private String town;

    @Column(length = 50)
    private String country; // in production, this would be a large enum with all countries

    @Column(length = 10)
    private String postcode;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "address")
    private List<Customer> customerList;

}
