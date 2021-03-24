package com.pc.model.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pc.common.Validations;
import com.pc.model.BaseModel;

public class BookingRequestDTO extends BaseModel {
	
	//regex for names https://stackoverflow.com/questions/35392798/regex-to-validate-full-name-having-atleast-four-characters
	@Pattern(regexp = Validations.NAME_REGEX)
	@NotNull
	private String name;

	//https://stackoverflow.com/questions/201323/how-to-validate-an-email-address-using-a-regular-expression
	@Pattern(regexp = Validations.EMAIL_REGEX)
	@NotNull
	private String email;
	
	@DateTimeFormat(pattern = Validations.DATE_REGEX)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Validations.DATE_PATTERN)
	@NotNull
	private LocalDate startInclusive;
	
	@DateTimeFormat(pattern = Validations.DATE_REGEX)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Validations.DATE_PATTERN)
	@NotNull
	private LocalDate endInclusive;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getStartInclusive() {
		return startInclusive;
	}

	public void setStartInclusive(LocalDate startInclusive) {
		this.startInclusive = startInclusive;
	}

	public LocalDate getEndInclusive() {
		return endInclusive;
	}

	public void setEndInclusive(LocalDate endInclusive) {
		this.endInclusive = endInclusive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}

