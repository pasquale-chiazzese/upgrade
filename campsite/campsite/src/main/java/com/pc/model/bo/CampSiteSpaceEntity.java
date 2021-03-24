package com.pc.model.bo;

import java.time.LocalDate;

public class CampSiteSpaceEntity extends BaseEntity {

	private String refernceId;
	private LocalDate reservedDate;
	private String holderEmail;

	public LocalDate getReservedDate() {
		return reservedDate;
	}

	public void setReservedDate(LocalDate reservedDate) {
		this.reservedDate = reservedDate;
	}

	public String getHolderEmail() {
		return holderEmail;
	}

	public void setHolderEmail(String holderEmail) {
		this.holderEmail = holderEmail;
	}

	public String getRefernceId() {
		return refernceId;
	}

	public void setRefernceId(String refernceId) {
		this.refernceId = refernceId;
	}	
	
}

