package com.pc.model.transform;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pc.model.bo.BookingEntity;
import com.pc.model.bo.CampSiteSpaceEntity;
import com.pc.model.dto.BookingDTO;

@Component
public class BookingTransformer {

	@Autowired
	private CampSiteSpaceTransformer campSiteSpaceTransformer;
	
	public BookingDTO boTOdto(BookingEntity bo) {
		
		BookingDTO booking = new BookingDTO();
		booking.setRefernceId(bo.getRefernceId());
		booking.setEmail(bo.getEmail());
		booking.setFullName(bo.getFullName());
		
		List<CampSiteSpaceEntity> datesBooked = bo.getDatesBooked();
		booking.setDatesBooked(campSiteSpaceTransformer.boTOdto(datesBooked));
		
		return booking;
	}	
	
	
	public BookingEntity dtoToBo(BookingDTO dto) {

		BookingEntity copy = new BookingEntity();
		copy.setRefernceId(dto.getRefernceId());
		copy.setEmail(dto.getEmail());
		copy.setFullName(dto.getFullName());
		
		List<CampSiteSpaceEntity> datesBo = dto.getDatesBooked().stream().map(datesDto -> {
			try {
				CampSiteSpaceEntity cs = campSiteSpaceTransformer.dtoToBo(datesDto);
				return cs;
			} catch (Exception e) {
			}
			return null;
		}).collect(Collectors.toList());
		
		copy.setDatesBooked(datesBo);
		
		return copy;
	}	
	
	
}
