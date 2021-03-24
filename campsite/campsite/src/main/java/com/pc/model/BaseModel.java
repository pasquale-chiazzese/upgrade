package com.pc.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

abstract public class BaseModel {
	//NOTE: reflection is slower but at this point in development it's ok
	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);		
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this);
	}
	
}
