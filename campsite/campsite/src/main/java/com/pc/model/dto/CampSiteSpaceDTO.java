package com.pc.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pc.common.Validations;
import com.pc.model.BaseModel;

public class CampSiteSpaceDTO extends BaseModel {
	@NotNull
	private String refernceId;
	
	@DateTimeFormat(pattern = Validations.DATE_REGEX)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Validations.DATE_PATTERN)
	@NotNull
	private LocalDate reservedDate;
	
	@Pattern(regexp = Validations.EMAIL_REGEX)
	@NotNull
	private String holderEmail;
	
	public String getRefernceId() {
		return refernceId;
	}
	
	public void setRefernceId(String refernceId) {
		this.refernceId = refernceId;
	}
	
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
	
}
