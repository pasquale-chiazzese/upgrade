package com.pc.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pc.common.EntityLogicException;
import com.pc.common.UniqueIdGenerator;
import com.pc.controller.RestExceptionHandler;
import com.pc.dal.BookingsStore;
import com.pc.dal.SpacesStore;
import com.pc.model.bo.BookingEntity;
import com.pc.model.bo.CampSiteAvailableEntity;
import com.pc.model.bo.CampSiteSpaceEntity;
import com.pc.service.SiteService;

@Component
public class SiteServiceImpl implements SiteService {
	private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);
	
	@Autowired
	private SpacesStore spacesStore;	
	
	@Autowired
	private BookingsStore bookingsStore;
	
	@Autowired
	private UniqueIdGenerator uuid;
	
	@Override
	public List<CampSiteSpaceEntity> findAllCampSiteSpace() {
		log.info("- entering");
		
		List<CampSiteSpaceEntity> findAll = spacesStore.findAll();
		findAll.sort((e1, e2) -> e1.getReservedDate().compareTo(e2.getReservedDate()));
		return findAll;
	}
	
	@Override
	public List<CampSiteSpaceEntity> findCampSiteSpacesByRrefernceId(String refernceId) {
		log.info("- entering {}", refernceId);
		return spacesStore.findByRefernceId(refernceId);
	}
	
	@Override
	public CampSiteSpaceEntity findCampSiteSpacesByRrefernceIdDate(String refernceId, LocalDate date) {
		log.info("- entering {} {}", refernceId, date);
		return spacesStore.findCampSiteSpacesByRrefernceIdDate(refernceId, date);
	}
	
	@Override
	public List<CampSiteAvailableEntity> findAvaiableSpaces() {
		log.info("- entering");
		//no same day avaiabality and up to 1 month away
		return findAvaiableSpaces(LocalDate.now().plus(1, ChronoUnit.DAYS), 
									LocalDate.now().plus(1, ChronoUnit.MONTHS));
	}
	
	@Override
	public List<CampSiteAvailableEntity> findAvaiableSpaces(LocalDate startInclusive, LocalDate endInclusive) {
		log.info("- entering start:{} end{}", startInclusive, endInclusive);
		
		LocalDate start = startInclusive;
		LocalDate end = endInclusive;
		//find currently booked dates
		List<CampSiteSpaceEntity> findByDateRange = spacesStore.findByDateRange(start, end);
		Set<LocalDate> currentBookedDates = findByDateRange.stream().map(bo -> bo.getReservedDate()).collect(Collectors.toSet());
		
		ArrayList<CampSiteAvailableEntity> freeDates = new ArrayList<>();
		while(start.isBefore(end)) {
			if(!currentBookedDates.contains(start)) {
				CampSiteAvailableEntity freeDate = new CampSiteAvailableEntity();
				freeDate.setDate(start);
				freeDates.add(freeDate);
			}
			start = start.plus(1, ChronoUnit.DAYS);
		}
		
		return freeDates;
	}
	
	@Override
	public BookingEntity findBooking(String referenceId) {
		log.info("- entering {}", referenceId);
		BookingEntity findByRefernceId = bookingsStore.findByRefernceId(referenceId);
		return findByRefernceId;
	}	
	
	@Override
	synchronized public BookingEntity cancelBooking(String referenceId) {
		log.info("- entering {}", referenceId);
		BookingEntity toDelete = bookingsStore.findByRefernceId(referenceId);
		if(toDelete == null) {
			return null;
		}
		spacesStore.delete(toDelete.getDatesBooked());
		bookingsStore.delete(toDelete.getId());
		return toDelete;
	}
	
	@Override
	synchronized public BookingEntity updateBooking(BookingEntity bookingToUpdate) {
		
		BookingEntity oldBe = bookingsStore.findByRefernceId(bookingToUpdate.getRefernceId());
		if(oldBe == null) {
			throw new EntityLogicException("booking to update was not found");
		}
		
		//first validate the updated booking dates are logically correct
		List<CampSiteSpaceEntity> datesBooked = bookingToUpdate.getDatesBooked();
		
		if(CollectionUtils.isEmpty(datesBooked)) {
			throw new EntityLogicException("booking to update has no dates");
		}
		if(datesBooked.size() > 3) {
			throw new EntityLogicException("booking to update has more than 3 days");
		}
		if(!areDatesConsecutive(datesBooked)) {
			throw new EntityLogicException("booking to update dates are not consecutive");
		}
		if(!ensureDatesAreAvailable(datesBooked)) {
			throw new EntityLogicException("booking to update not all dates are available");
		}
		if(!ensureSpacesMatchReference(bookingToUpdate.getRefernceId(), datesBooked)) {
			throw new EntityLogicException("camp site dates do not match booking reference", 
											"ensure all camp site dates belong and match the booking reference");
		}
		//realign the email for each camp site date
		datesBooked.forEach(bo -> {
			bo.setHolderEmail(bookingToUpdate.getEmail());
		});
		
		//Note: this is cutting corners.  we'll first delete the existing booking and it's dates
		//and then save a new updated version. implementing a Merge of the enetites and
		//then updating is complex.
		//
		//now save the booking
		cancelBooking(bookingToUpdate.getRefernceId());
		spacesStore.saveUpdate(datesBooked);
		BookingEntity be = bookingsStore.saveUpdate(bookingToUpdate);
		
		return be;
	}
	
	private boolean ensureSpacesMatchReference(String bookingRef, List<CampSiteSpaceEntity> datesBooked) {
		for (CampSiteSpaceEntity cs : datesBooked) {
			if(!StringUtils.equals(bookingRef, cs.getRefernceId())) {
				return false;
			}
		}
		return true;
	}
	
	boolean areDatesConsecutive(List<CampSiteSpaceEntity> datesBooked) {
		if(datesBooked.size() == 1) {
			return true;
		}
		TreeSet<LocalDate> sortedDates = new TreeSet<>();
		datesBooked.stream().forEach(bo -> {
			sortedDates.add(bo.getReservedDate());
		});
		
		LocalDate previous = null;
		for (LocalDate aDate : sortedDates) {
			if(previous == null) {
				previous = aDate;
			} else {
				long between = ChronoUnit.DAYS.between(previous, aDate);
				if(between != 1) {
					return false;
				}
			}
		}
		return true;
	}
	
	boolean ensureDatesAreAvailable(List<CampSiteSpaceEntity> datesBooked) {
		for (CampSiteSpaceEntity cs : datesBooked) {
			CampSiteSpaceEntity found = spacesStore.findByDate(cs.getReservedDate());
			if(found == null) {
				continue;
			}
			if(!cs.getRefernceId().equals(found.getRefernceId())) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	synchronized public BookingEntity bookingRequest(String name, String email, LocalDate startInclusive, LocalDate endInclusive) {
	
		if(!areDatesWithinRange(startInclusive, endInclusive)) {
			throw new EntityLogicException("dates are out of range");
		}
		
		//reserve the dates
		List<CampSiteSpaceEntity> range = spacesStore.findByDateRange(startInclusive, endInclusive);
		if(CollectionUtils.isNotEmpty(range)) {
			List<LocalDate> dates = range.stream().map(bo -> bo.getReservedDate()).collect(Collectors.toList());
			throw new EntityLogicException("not all dates available: " + StringUtils.join(dates, "|"));
		}
		
		String ref = uuid.generateIdNoDash(8);
		List<CampSiteSpaceEntity> toReserve = makeAndSaveCampSiteSpaces(startInclusive, endInclusive, ref, email);
		BookingEntity be = makeAndSaveBooking(ref, email, name, toReserve);
		return be;
	}
	
	List<CampSiteSpaceEntity> makeAndSaveCampSiteSpaces(LocalDate startInclusive, LocalDate endInclusive, String ref, String email) {
		LocalDate start = startInclusive;
		ArrayList<CampSiteSpaceEntity> toReserve = new ArrayList<>();
		do {
			CampSiteSpaceEntity cs = new CampSiteSpaceEntity();
			cs.setRefernceId(ref);
			cs.setReservedDate(start);
			cs.setHolderEmail(email);
			toReserve.add(cs);
			start = start.plus(1, ChronoUnit.DAYS);
		} while(start.isBefore(endInclusive) || start.equals(endInclusive));
		
		//the dates have been saved
		spacesStore.saveUpdate(toReserve);
		
		return toReserve;
	}
	
	BookingEntity makeAndSaveBooking(String ref, String email, String name, List<CampSiteSpaceEntity> toReserve) {
		BookingEntity be = new BookingEntity();
		be.setRefernceId(ref);
		be.setEmail(email);
		be.setFullName(name);
		be.setDatesBooked(toReserve);
		
		bookingsStore.saveUpdate(be);
		return be;
	}
	
	boolean areDatesWithinRange(LocalDate startInclusive, LocalDate endInclusive) {
		LocalDate now = LocalDate.now();
		if(startInclusive.isBefore(now) ||
				endInclusive.isBefore(now)) {
			throw new EntityLogicException("a date is in the past");
		}
		if(startInclusive.equals(now)) {
			throw new EntityLogicException("no same day booking for start date");
		}
		if(startInclusive.isAfter(endInclusive)) {
			throw new EntityLogicException("start date after end date");
		}
		LocalDate oneMonthFuture = now.plus(1, ChronoUnit.MONTHS);
		if(startInclusive.isAfter(oneMonthFuture)) {
			throw new EntityLogicException("start date greater than 1 month away");
		}
		if(startInclusive.equals(endInclusive)) {
			//1 day
			return true;
		}		
		
		long between = ChronoUnit.DAYS.between(startInclusive, endInclusive);
		between = between + 1;
		if(between <= 3) {
			return true;
		}
		
		throw new EntityLogicException("more than 3 days requested");
	}
	
}

