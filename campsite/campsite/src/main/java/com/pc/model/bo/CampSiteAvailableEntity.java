package com.pc.model.bo;

import java.time.LocalDate;

import com.pc.model.BaseModel;

/**
 * This class is not a data business object, meaning it does not map
 * to a database table.
 * 
 * It's a business logic entity used to represent the system data.
 *
 */
public class CampSiteAvailableEntity extends BaseModel {
	
	private LocalDate date;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
