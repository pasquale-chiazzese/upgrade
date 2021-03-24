package com.pc.model.transform;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.pc.model.bo.CampSiteAvailableEntity;
import com.pc.model.dto.CampSiteAvailableDTO;

/**
 * Converts DTO to BO and vice versa.
 * 
 * (I could have used https://mapstruct.org/ but I haven't used it much recently
 * and felt it was over kill)
 * 
 */
@Component
public class CampSiteAvailableTransformer {

	public CampSiteAvailableDTO boTOdto(CampSiteAvailableEntity bo) {
		CampSiteAvailableDTO csa = new CampSiteAvailableDTO();
		csa.setDate(bo.getDate());
		return csa;
	}
	
	public List<CampSiteAvailableDTO> boTOdto(List<CampSiteAvailableEntity> bos) {
		if(CollectionUtils.isEmpty(bos)) {
			return Collections.emptyList();
		}
		return bos.stream().map(bo -> boTOdto(bo)).collect(Collectors.toList());
	}
}
