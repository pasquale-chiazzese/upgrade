package com.pc.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pc.common.Validations;
import com.pc.model.BaseModel;

/**
 * This class is overkill but leaving for completeness as other
 * attributes of a free space can arise.
 *
 * This DTO does not strictly map to a database entity.
 * 
 */
public class CampSiteAvailableDTO extends BaseModel {
	
	@DateTimeFormat(pattern = Validations.DATE_REGEX)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate date;

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
