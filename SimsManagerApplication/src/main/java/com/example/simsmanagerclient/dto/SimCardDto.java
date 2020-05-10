package com.example.simsmanagerclient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.simsmanagerclient.entities.SimCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class SimCardDto extends SimCardCreateDto {
    private final Long id;

    private final LocalDateTime createdDate;
    private final LocalDateTime firstUsedDate;

    private final String currentDeviceIMEI;
    private final String currentDeviceType;

    private final LocalDateTime lastUpdatedDate;

    public SimCardDto(final Long id,
                      final String ICCID,
                      final String IMSI,
                      final String MSISDN,
                      final Integer PIN,
                      final Integer PUC,
                      final CustomerDto customerDto,
                      final String vendor,
                      final String firstDeviceIMEI,
                      final String firstDeviceType,
                      final LocalDateTime createdDate,
                      final LocalDateTime firstUsedDate,
                      final String currentDeviceIMEI,
                      final String currentDeviceType,
                      final LocalDateTime lastUpdatedDate) {
        super(ICCID, IMSI, MSISDN, PIN, PUC, customerDto, vendor, firstDeviceIMEI, firstDeviceType);
        this.id = id;
        this.createdDate = createdDate;
        this.firstUsedDate = firstUsedDate;
        this.currentDeviceIMEI = currentDeviceIMEI;
        this.currentDeviceType = currentDeviceType;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * Excluding PIN, PUC and CustomerCreateDto from JSON serialization.
     * @param simCard - sim Card as retrieved from database
     * @return sim Card with exluded PIN, PUC and customer info (null values are excluded from JSON)
     */
    public static SimCardDto transform(final SimCard simCard) {
        return new SimCardDto(simCard.getId(),
                                simCard.getICCID(),
                                simCard.getIMSI(),
                                simCard.getMSISDN(),
                                null,
                                null,
                                simCard.getCustomer() == null ? null : CustomerDto.transform(simCard.getCustomer()),
                                simCard.getVendor(),
                                simCard.getFirstDeviceIMEI(),
                                simCard.getFirstDeviceType(),
                                simCard.getCreatedDate(),
                                simCard.getFirstUsedDate(),
                                simCard.getCurrentDeviceIMEI(),
                                simCard.getCurrentDeviceType(),
                                simCard.getLastUpdatedDate());
    }
}
