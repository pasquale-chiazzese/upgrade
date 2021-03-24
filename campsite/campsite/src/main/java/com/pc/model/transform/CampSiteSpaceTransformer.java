package com.pc.model.transform;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.pc.model.bo.CampSiteSpaceEntity;
import com.pc.model.dto.CampSiteSpaceDTO;

/**
 * Converts DTO to BO and vice versa.
 * 
 * (I could have used https://mapstruct.org/ but I haven't used it much recently
 * and felt it was over kill)
 * 
 */
@Component
public class CampSiteSpaceTransformer {
	
	public CampSiteSpaceDTO boTOdto(CampSiteSpaceEntity bo) {
		CampSiteSpaceDTO dto = new CampSiteSpaceDTO();
		dto.setRefernceId(bo.getRefernceId());
		dto.setReservedDate(bo.getReservedDate());
		dto.setHolderEmail(bo.getHolderEmail());
		return dto;
	}
	
	public List<CampSiteSpaceDTO> boTOdto(List<CampSiteSpaceEntity> bos) {
		if(CollectionUtils.isEmpty(bos)) {
			return Collections.emptyList();
		}
		return bos.stream().map(bo -> boTOdto(bo)).collect(Collectors.toList());
	}
	
	public CampSiteSpaceEntity dtoToBo(CampSiteSpaceDTO dto) {
		CampSiteSpaceEntity target = new CampSiteSpaceEntity();
		target.setReservedDate(dto.getReservedDate());
		target.setRefernceId(dto.getRefernceId());
		target.setHolderEmail(dto.getHolderEmail());
		return target;
	}	
	
	
}

