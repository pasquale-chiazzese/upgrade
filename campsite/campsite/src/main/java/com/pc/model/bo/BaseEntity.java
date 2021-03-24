package com.pc.model.bo;

import java.time.LocalDateTime;

import com.pc.model.BaseModel;

abstract public class BaseEntity extends BaseModel {
	
	private Integer id;
	private LocalDateTime created = LocalDateTime.now();
	private LocalDateTime updated = LocalDateTime.now();
	private Integer version = Integer.valueOf(0);
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public LocalDateTime getCreated() {
		return created;
	}
	
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	
	public LocalDateTime getUpdated() {
		return updated;
	}
	
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
