package com.pc.dal;

import java.time.LocalDate;
import java.util.List;

import com.pc.model.bo.CampSiteSpaceEntity;

public interface SpacesStore {
	
	CampSiteSpaceEntity saveUpdate(CampSiteSpaceEntity entity);
	List<CampSiteSpaceEntity> saveUpdate(List<CampSiteSpaceEntity> entities);
	CampSiteSpaceEntity delete(Integer id);
	List<CampSiteSpaceEntity> delete(List<CampSiteSpaceEntity> entities);
	CampSiteSpaceEntity findById(Integer id);
	List<CampSiteSpaceEntity> findAll();
	List<CampSiteSpaceEntity> findByRefernceId(String referenceId);
	CampSiteSpaceEntity findCampSiteSpacesByRrefernceIdDate(String refernceId, LocalDate date);
	List<CampSiteSpaceEntity> findByDateRange(LocalDate startInclusive, LocalDate endInclusive);
	CampSiteSpaceEntity findByDate(LocalDate date);
	
}
