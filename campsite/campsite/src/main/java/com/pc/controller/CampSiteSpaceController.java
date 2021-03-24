package com.pc.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pc.common.EntityLogicException;
import com.pc.model.bo.CampSiteSpaceEntity;
import com.pc.model.dto.CampSiteSpaceDTO;
import com.pc.model.transform.CampSiteSpaceTransformer;
import com.pc.service.SiteService;

/**
 * This controller can be seen as the micro service data domain
 * controller.  Which governs the data and access.  
 * 
 * NOTE: this controller is used mainly for testing in this project
 *
 * Controller Response based on https://www.baeldung.com/spring-boot-json
 * 
 *  
 */
@Controller
public class CampSiteSpaceController {
	private static final Logger log = LoggerFactory.getLogger(CampSiteSpaceController.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private CampSiteSpaceTransformer campSiteSpaceTransformer;
	
    @GetMapping(value = "api/v1/campsitespaces")
    public ResponseEntity<List<CampSiteSpaceDTO>> findAllCampSiteSpace() {
    	log.info("- entering");
    	List<CampSiteSpaceEntity> spacesEntity = siteService.findAllCampSiteSpace();
    	if(CollectionUtils.isEmpty(spacesEntity)) {
    		log.error("no camp spaces");
    		throw new EntityLogicException("no camp spaces", "add some reservations");
    	}
        return ResponseEntity.ok(campSiteSpaceTransformer.boTOdto(spacesEntity));
    }

    @GetMapping(value = "api/v1/campsitespaces/{refernceId}")
    public ResponseEntity<List<CampSiteSpaceDTO>> findCampSiteSpacesByRrefernceId(@PathVariable String refernceId) {
    	log.info("- entering {}", refernceId);
    	List<CampSiteSpaceEntity> spacesEntity = siteService.findCampSiteSpacesByRrefernceId(refernceId);
    	if(CollectionUtils.isEmpty(spacesEntity)) {
    		log.error("no camp spaces for refernceId");
    		throw new EntityLogicException("no camp spaces for refernceId", "no booking matching refernceId");
    	}
        return ResponseEntity.ok(campSiteSpaceTransformer.boTOdto(spacesEntity));
    }
    
}

