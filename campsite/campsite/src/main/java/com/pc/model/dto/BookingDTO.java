package com.pc.model.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.pc.common.Validations;
import com.pc.model.BaseModel;

public class BookingDTO extends BaseModel {
	@NotNull
	private String refernceId;
	
	@NotNull
	@Pattern(regexp = Validations.NAME_REGEX)
	private String fullName;
	
	@NotNull
	@Pattern(regexp = Validations.EMAIL_REGEX)
	private String email;
	
	@NotNull
	private List<CampSiteSpaceDTO> datesBooked;
	
	public String getRefernceId() {
		return refernceId;
	}
	
	public void setRefernceId(String refernceId) {
		this.refernceId = refernceId;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<CampSiteSpaceDTO> getDatesBooked() {
		return datesBooked;
	}
	
	public void setDatesBooked(List<CampSiteSpaceDTO> datesBooked) {
		this.datesBooked = datesBooked;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
}
