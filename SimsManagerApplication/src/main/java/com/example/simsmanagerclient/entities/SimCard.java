package com.example.simsmanagerclient.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
public class SimCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iccid", length = 20)
    private String ICCID;

    @Column(name  = "imsi", length = 15)
    private String IMSI;

    @Column(name = "msisdn", length = 15)
    private String MSISDN;

    @Column(name = "pin")
    private Integer PIN;

    @Column(name = "puc")
    private Integer PUC;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "vendor", length = 50)
    private String vendor;

    @Column(name = "first_used_date")
    private LocalDateTime firstUsedDate;

    @Column(name = "first_device_imei", length = 15)
    private String firstDeviceIMEI;

    @Column(name = "first_device_type", length = 50)
    private String firstDeviceType;

    @Column(name = "device_imei", length = 15)
    private String currentDeviceIMEI;

    @Column(name = "device_type", length = 50)
    private String currentDeviceType;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

}
