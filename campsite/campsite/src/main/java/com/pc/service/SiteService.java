package com.pc.service;

import java.time.LocalDate;
import java.util.List;

import com.pc.model.bo.BookingEntity;
import com.pc.model.bo.CampSiteAvailableEntity;
import com.pc.model.bo.CampSiteSpaceEntity;

public interface SiteService {

	List<CampSiteSpaceEntity> findAllCampSiteSpace();
	List<CampSiteSpaceEntity> findCampSiteSpacesByRrefernceId(String refernceId);
	CampSiteSpaceEntity findCampSiteSpacesByRrefernceIdDate(String refernceId, LocalDate date);
	
	List<CampSiteAvailableEntity> findAvaiableSpaces();
	List<CampSiteAvailableEntity> findAvaiableSpaces(LocalDate startInclusive, LocalDate endInclusive);
	
	BookingEntity findBooking(String referenceId);
	BookingEntity cancelBooking(String referenceId);
	BookingEntity updateBooking(BookingEntity bookingEntity);
	
	BookingEntity bookingRequest(String name, String email, LocalDate startInclusive, LocalDate endInclusive);
	
}
