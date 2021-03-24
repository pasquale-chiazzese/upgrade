package com.pc.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pc.common.EntityLogicException;
import com.pc.model.bo.BookingEntity;
import com.pc.model.bo.CampSiteAvailableEntity;
import com.pc.model.dto.BookingDTO;
import com.pc.model.dto.BookingRequestDTO;
import com.pc.model.dto.CampSiteAvailableDTO;
import com.pc.model.transform.BookingTransformer;
import com.pc.model.transform.CampSiteAvailableTransformer;
import com.pc.service.SiteService;

/**
 * This controller is the appliction logic access.  It can be seen as
 * the Backend-For-Front orchestrator (BFF) pattern serving specific data sets
 * for a front end application and entry point for the business logic.
 *
 * Controller Response based on https://www.baeldung.com/spring-boot-json
 */
@Controller
public class CampSiteController {
	private static final Logger log = LoggerFactory.getLogger(CampSiteController.class);
	
	@Autowired
	private SiteService siteService;

	@Autowired
	private CampSiteAvailableTransformer campSiteAvailableTransformer;
	
	@Autowired
	private BookingTransformer bookingTransformer;
	
    @GetMapping(value = "api/v1/campsite")
    public ResponseEntity<List<CampSiteAvailableDTO>> findAvaiableSpaces() {
    	log.info("- entering");
    	
    	List<CampSiteAvailableEntity> avaiableSpaces = siteService.findAvaiableSpaces();
    	if(CollectionUtils.isEmpty(avaiableSpaces)) {
    		log.error("no camp spaces avaiable");
    		throw new EntityLogicException("no camp spaces avaiable", "free some camp space dates");
    	}    	
    	return ResponseEntity.ok(campSiteAvailableTransformer.boTOdto(avaiableSpaces));
    }
    
    @GetMapping(value = "api/v1/campsite/{startInclusive}/{endInclusive}")
    public ResponseEntity<List<CampSiteAvailableDTO>> findAvaiableSpaces(
						    							@PathVariable LocalDate startInclusive, 
						    							@PathVariable LocalDate endInclusive) {
    	log.info("- entering start: {} end: {}", startInclusive, endInclusive);
    	
    	List<CampSiteAvailableEntity> avaiableSpaces = siteService.findAvaiableSpaces(startInclusive, endInclusive);
    	if(CollectionUtils.isEmpty(avaiableSpaces)) {
    		log.error("no camp spaces avaiable");
    		throw new EntityLogicException("no camp spaces avaiable", "free some camp space dates");
    	}    	
    	return ResponseEntity.ok(campSiteAvailableTransformer.boTOdto(avaiableSpaces));
    }
    
    @GetMapping(value = "api/v1/booking/{referenceId}")
    public ResponseEntity<BookingDTO> findBooking(@PathVariable String referenceId) {
    	log.info("- entering");
    	BookingEntity findBooking = siteService.findBooking(referenceId);
    	if(findBooking == null) {
    		log.error("booking not found");
    		throw new EntityLogicException("booking not found", "the booking does not exist");
    	}
        return ResponseEntity.ok(bookingTransformer.boTOdto(findBooking));
    }
    
    @DeleteMapping(value = "api/v1/booking/{referenceId}")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable String referenceId) {
    	log.info("- entering");
    	BookingEntity findBooking = siteService.cancelBooking(referenceId);
		if(findBooking == null) {
			throw new EntityLogicException("no booking to delete " + referenceId, "the booking was not found");
		}
        return ResponseEntity.ok(bookingTransformer.boTOdto(findBooking));
    }   
    
    @PutMapping(value = "api/v1/booking/{referenceId}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable String referenceId, 
    												@Valid @RequestBody BookingDTO bookingDTO) {
    	log.info("- entering");
		
		BookingEntity updateBooking = bookingTransformer.dtoToBo(bookingDTO);
		BookingEntity be = siteService.updateBooking(updateBooking);
		if(be == null) {
			throw new EntityLogicException("update booking failed", "the booking was not saved or updated");
		}
        return ResponseEntity.ok(bookingTransformer.boTOdto(be));
    }
    
    @PostMapping(value = "api/v1/bookingrequest")
    public ResponseEntity<BookingDTO> bookAvaiableSpaces(@Valid @RequestBody BookingRequestDTO bookingRequest) {
    	log.info("- entering");
    	BookingEntity br = siteService.bookingRequest(bookingRequest.getName(), 
    								bookingRequest.getEmail(), 
									bookingRequest.getStartInclusive(),
									bookingRequest.getEndInclusive());
		if(br == null) {
			throw new EntityLogicException("failed to book the request", "the booking was not saved, an unknown error occurred");
		}    	
    	
    	return ResponseEntity.ok(bookingTransformer.boTOdto(br));
    }    
    
    
}

