package com.example.simsmanagerclient.services;

import com.example.simsmanagerclient.dto.SimCardCreateDto;
import com.example.simsmanagerclient.dto.SimCardDto;
import com.example.simsmanagerclient.dto.SimCardUpdateDto;
import com.example.simsmanagerclient.entities.Customer;
import com.example.simsmanagerclient.entities.SimCard;
import com.example.simsmanagerclient.exception.SimCardBadRequestException;
import com.example.simsmanagerclient.exception.SimCardManagerException;
import com.example.simsmanagerclient.exception.SimCardOperationException;
import com.example.simsmanagerclient.repositories.SimCardRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Min;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
@AllArgsConstructor
@Service
public class SimCardService {

    public static final String SIMCARD_LINK_TO_CUSTOMER = "Sim Card with id:[%d] was successfully added to customer with id:[%d].";

    private final CustomerService customerService;
    private final SimCardRepository simCardRepository;

    /**
     * Creation of a new SIM card.
     * @param simCardDto the SIM Card data.
     * @return created SIM card.
     */
    public SimCardDto createSimCard(final @NonNull SimCardCreateDto simCardDto) {
        return SimCardDto.transform(simCardRepository.save(SimCardCreateDto.transform(simCardDto)));
    }

    /**
     * Update of a SIM card.
     * @param simCardDto the SIM card data to be updated.
     * @return updated SIM card.
     * @throws SimCardManagerException exception if SIM Card not found or cannot be updated.
     */
    @Transactional
    public SimCardDto updateSimCard(final @Min(1L) Long simCardId,
                                    final @NonNull SimCardUpdateDto simCardDto)
                                    throws SimCardManagerException {
        if (!simCardId.equals(simCardDto.getId())) {
            log.error("Mismatched Sim card ids found:[{}] [{}]", simCardId, simCardDto.getId());
            throw new SimCardBadRequestException("Mismatch in Sim Card Ids found.");
        }
        if (simCardDto.getCustomerUpdateDto() != null) {
            customerService.updateCustomer(simCardDto.getCustomerUpdateDto().getId(),
                                            simCardDto.getCustomerUpdateDto());
        }
        final SimCard simCard = simCardRepository.findById(simCardDto.getId()).orElseThrow(() ->
                new SimCardOperationException(format("SimCard with id:[%d] was not found.", simCardDto.getId())));
        log.info(format("Updating SIM Card with id:[%d]", simCard.getId()));
        return SimCardDto.transform(simCardRepository.save(SimCardUpdateDto.updateFields(simCard, simCardDto)));
    }

    /**
     * Retrieves SIM Card by id.
     * @param simCardId id of the SIM Card.
     * @return SIM card object.
     * @throws SimCardOperationException exception.
     */
    public SimCard findSimCard(final @Min(1L) Long simCardId) throws SimCardOperationException {
        return simCardRepository.findById(simCardId).orElseThrow(() ->
                new SimCardOperationException(format("SimCard with id:%d was not found.", simCardId)));
    }

    /**
     * Links a customer to a SIM Card.
     * @param simCardId the id of the SIM card.
     * @param customerId the id of the customer.
     * @return SIM card with the linked customer.
     * @throws SimCardManagerException exception
     */
    public String linkSimCardToCustomer(final @Min(1L) Long simCardId, final @Min(1L) Long customerId)
                                                                throws SimCardManagerException {
        final SimCard simCard = findSimCard(simCardId);
        final Customer customer = customerService.findCustomerById(customerId);
        simCard.setCustomer(customer);
        final SimCard updatedSimCard = simCardRepository.save(simCard);
        if (updatedSimCard.getCustomer().getId().equals(customerId)) {
            return format(SIMCARD_LINK_TO_CUSTOMER, simCardId, customerId);
        }
        final String errorMessage = format(
            "An error has occurred while updating Sim Card with id:[%d] to customer with id:[%d].", simCardId, customerId);
        log.error(errorMessage);
        throw new SimCardOperationException(errorMessage);
    }

    /**
     * Finds all customer SIM cards.
     * @param customerId id of the customer for whom SIM cards are required.
     * @return list of SIM cards pertaining to the customer.
     */
    public List<SimCardDto> findSimCardsByCustomerId(final @Min(1L) Long customerId) {
        return simCardRepository.findByCustomerId(customerId).stream()
                                                            .map(SimCardDto::transform)
                                                            .collect(toList());
    }

    /**
     * Find all SIM cards.
     * @return all SIM Cards
     */
    public List<SimCardDto> findAllSims() {
        return simCardRepository.findAll().stream()
                                            .map(SimCardDto::transform)
                                            .collect(toList());
    }

    /**
     * Find SIM cards by Pageable {@link Pageable}.
     * @param pageable the pagination object by which SIM cards are required.
     * @return SIM cards by Page object
     */
    public List<SimCardDto> findAllSimsByPage(final @NonNull Pageable pageable) {
        return simCardRepository.findAll(pageable).stream()
                                                    .map(SimCardDto::transform)
                                                    .collect(toList());
    }
}
