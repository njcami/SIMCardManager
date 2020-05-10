package com.example.simsmanagerclient.dto;

import com.example.simsmanagerclient.entities.Customer;
import com.example.simsmanagerclient.entities.SimCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimCardCreateDto {

    @NotBlank(message = "Please enter SIM Card ICCID.")
    private String ICCID;

    @NotBlank(message = "Please enter Sim Card IMSI.")
    private String IMSI;

    private String MSISDN;

    @Min(value = 100, message = "Please enter a minimum of 3 digits.")
    @Max(value = 9999, message = "Please enter 4 digits or less.")
    private Integer PIN;

    @Min(value = 100000, message = "Please enter a minimum of 6 digits.")
    @Max(value = 99999999, message = "Please enter 8 digits or less.")
    private Integer PUC;

    private CustomerDto customerDto;

    private String vendor;

    private String firstDeviceIMEI;

    private String firstDeviceType;

    public static SimCard transform(final @NonNull SimCardCreateDto simCardDto) {
        final SimCard simCard = SimCard.builder()
                                    .ICCID(simCardDto.getICCID())
                                    .IMSI(simCardDto.getIMSI())
                                    .MSISDN(simCardDto.getMSISDN())
                                    .PIN(simCardDto.getPIN())
                                    .PUC(simCardDto.getPUC())
                                    .vendor(simCardDto.getVendor())
                                    .createdDate(LocalDateTime.now())
                                    .customer(CustomerDto.transform(simCardDto.getCustomerDto()))
                                    .build();

        // This checks if the SIM Card has been used with a device
        if (!StringUtils.isEmpty(simCardDto.getFirstDeviceIMEI())) {
            simCard.setFirstDeviceIMEI(simCardDto.getFirstDeviceIMEI());
            simCard.setFirstDeviceType(simCardDto.getFirstDeviceType());
            simCard.setCurrentDeviceIMEI(simCardDto.getFirstDeviceIMEI());
            simCard.setCurrentDeviceType(simCardDto.getFirstDeviceType());
            simCard.setFirstUsedDate(LocalDateTime.now());
        }
        return simCard;
    }

}
