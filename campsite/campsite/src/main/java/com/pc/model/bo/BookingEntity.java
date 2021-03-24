package com.pc.model.bo;

import java.util.List;

public class BookingEntity extends BaseEntity {

	private String refernceId;
	private String email;
	private String fullName;
	private List<CampSiteSpaceEntity> datesBooked;
	
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
	
	public List<CampSiteSpaceEntity> getDatesBooked() {
		return datesBooked;
	}
	
	public void setDatesBooked(List<CampSiteSpaceEntity> datesBooked) {
		this.datesBooked = datesBooked;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
}
