package com.example.simsmanagerclient.dto;

import com.example.simsmanagerclient.entities.SimCard;
import com.example.simsmanagerclient.exception.SimCardBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimCardUpdateDto {

    @Min(1L)
    private Long id;

    @NotBlank
    private String ICCID; // as a double check to confirm that the correct id has been entered

    private String MSISDN;
    private CustomerUpdateDto customerUpdateDto;

    private LocalDateTime firstUsedDate;

    private String currentDeviceIMEI;
    private String currentDeviceType;

    public static SimCard updateFields(final @NonNull SimCard simCardToUpdate,
                                       final @NonNull SimCardUpdateDto simCardDto)
                                            throws SimCardBadRequestException {
        if (simCardToUpdate.getICCID().equals(simCardDto.getICCID())) {
            simCardToUpdate.setMSISDN(simCardDto.getMSISDN());
            simCardToUpdate.setFirstUsedDate(simCardDto.getFirstUsedDate());
            simCardToUpdate.setCurrentDeviceType(simCardDto.getCurrentDeviceType());
            simCardToUpdate.setCurrentDeviceIMEI(simCardDto.getCurrentDeviceIMEI());
            simCardToUpdate.setLastUpdatedDate(LocalDateTime.now());
            return simCardToUpdate;
        }
        throw new SimCardBadRequestException("Update SimCard ICCID and existing ICCID do not match.");
    }

}
