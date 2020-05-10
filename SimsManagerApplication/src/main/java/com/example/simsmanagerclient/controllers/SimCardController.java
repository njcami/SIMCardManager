package com.example.simsmanagerclient.controllers;

import com.example.simsmanagerclient.dto.SimCardCreateDto;
import com.example.simsmanagerclient.dto.SimCardDto;
import com.example.simsmanagerclient.dto.SimCardUpdateDto;
import com.example.simsmanagerclient.exception.SimCardManagerException;
import com.example.simsmanagerclient.services.SimCardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
@RestController
public class SimCardController {

    private final SimCardService simCardService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/simcard", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public SimCardDto createSimCard(final @NonNull @RequestBody SimCardCreateDto simCard) {
        return simCardService.createSimCard(simCard);
    }

    @PutMapping(value = "/simcard/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<SimCardDto> updateSimCard(final @PathVariable Long simCardId,
                                    final @NonNull @RequestBody SimCardUpdateDto simCard) throws SimCardManagerException {
        if (simCardId <  1L) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(simCardService.updateSimCard(simCardId, simCard), OK);
    }

    @GetMapping(value = "/simcard/{id}/link-to-customer/{customerId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> linkSimCardToCustomer(final @PathVariable Long id,
                                                       final @PathVariable Long customerId)
                                            throws SimCardManagerException {
        if (id < 1L || customerId < 1L) {
            return new ResponseEntity<>("Ids have to be greater than zero", BAD_REQUEST);
        }
        return new ResponseEntity<>(simCardService.linkSimCardToCustomer(id, customerId), OK);
    }

    @GetMapping(value = "/simcard", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimCardDto>> retrieveAllSimCardsForCustomer(final @RequestParam Long customerId) {
        if (customerId <  1L) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(simCardService.findSimCardsByCustomerId(customerId), OK);
    }

    @ResponseStatus(OK)
    @GetMapping(value = "/simcard/all", produces = APPLICATION_JSON_VALUE)
    public List<SimCardDto> retrieveAllSimCards() {
        return simCardService.findAllSims();
    }

    @ResponseStatus(OK)
    @GetMapping(value = "/simcard/paged", produces = APPLICATION_JSON_VALUE)
    public List<SimCardDto> retrieveSimCardsByPage(final @NonNull Pageable pageable) {
        return simCardService.findAllSimsByPage(pageable);
    }
}
