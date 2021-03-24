package com.pc.dal.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.pc.dal.SpacesStore;
import com.pc.model.bo.CampSiteSpaceEntity;

/**
 * Simulate the Data Access Layer with no real database.
 * 
 * The data store will have concurrent reads but single writes.
 *
 */
@Repository
public class SpacesStoreImpl extends BaseDALStoreImpl<CampSiteSpaceEntity> implements SpacesStore {
	
	//NOTE: I use an Enumeration since they are NOT fail-fast (like iterators in general) 
	//and with the ConcurrentHashMap we should have no issues with multi threading,
	//in order to achieve multi read and single write isolation.
	//I could not confirm if streams or lambda provide the same, it's not
	//clear in the doc or implementation regarding this.  Went with what I know.
	@Override
	public List<CampSiteSpaceEntity> findByRefernceId(String refernceId) {
		ArrayList<CampSiteSpaceEntity> match = new ArrayList<>();
		Enumeration<CampSiteSpaceEntity> elements = dataBase.elements();
		while(elements.hasMoreElements()) {
			CampSiteSpaceEntity elem = elements.nextElement();
			if(StringUtils.equals(refernceId, elem.getRefernceId())) {
				match.add(elem);
			}
		}
		return match;
	}
	
	@Override
	public List<CampSiteSpaceEntity> findByDateRange(LocalDate startInclusive, LocalDate endInclusive) {
		
		ArrayList<CampSiteSpaceEntity> match = new ArrayList<>();
		Enumeration<CampSiteSpaceEntity> elements = dataBase.elements();
		while(elements.hasMoreElements()) {
			CampSiteSpaceEntity elem = elements.nextElement();
			LocalDate reservedDate = elem.getReservedDate();
			if((reservedDate.equals(startInclusive) || reservedDate.isAfter(startInclusive)) &&
					(reservedDate.equals(endInclusive) || reservedDate.isBefore(endInclusive))) {
				match.add(elem);
			}
		}
		return match;
	}
	
	@Override
	public CampSiteSpaceEntity findByDate(LocalDate date) {
		
		Enumeration<CampSiteSpaceEntity> elements = dataBase.elements();
		while(elements.hasMoreElements()) {
			CampSiteSpaceEntity elem = elements.nextElement();
			LocalDate reservedDate = elem.getReservedDate();
			if(reservedDate.equals(date)) {
				return elem;
			}
		}
		return null;
	}
	
	@Override
	public CampSiteSpaceEntity findCampSiteSpacesByRrefernceIdDate(String refernceId, LocalDate date) {
		Enumeration<CampSiteSpaceEntity> elements = dataBase.elements();
		while(elements.hasMoreElements()) {
			CampSiteSpaceEntity elem = elements.nextElement();
			LocalDate reservedDate = elem.getReservedDate();
			if(reservedDate.equals(date) && StringUtils.equals(refernceId, elem.getRefernceId())) {
				return elem;
			}
		}
		return null;
	}
	
	@Override
	synchronized protected void ensureBeforeNewAddConstraints(CampSiteSpaceEntity newEntity) {
		super.ensureBeforeNewAddConstraints(newEntity);
		//we have a simple constraint before adding a new enity that the date is not
		//already taken
		CampSiteSpaceEntity find = findByDate(newEntity.getReservedDate());
		if(find != null) {
			throw new RuntimeException("The date is alreay reserved");
		}
	}
	
}

