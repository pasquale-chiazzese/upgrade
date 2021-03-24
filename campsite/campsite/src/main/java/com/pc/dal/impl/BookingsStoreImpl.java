package com.pc.dal.impl;

import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.pc.dal.BookingsStore;
import com.pc.model.bo.BookingEntity;

@Repository
public class BookingsStoreImpl extends BaseDALStoreImpl<BookingEntity> implements BookingsStore {

	@Override
	public BookingEntity findByRefernceId(String refernceId) {
		Enumeration<BookingEntity> elements = dataBase.elements();
		while(elements.hasMoreElements()) {
			BookingEntity elem = elements.nextElement();
			if(StringUtils.equals(refernceId, elem.getRefernceId())) {
				return elem;
			}
		}
		return null;
	}
	
	
}

