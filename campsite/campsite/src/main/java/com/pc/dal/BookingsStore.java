package com.pc.dal;

import com.pc.model.bo.BookingEntity;

public interface BookingsStore {
	BookingEntity saveUpdate(BookingEntity entity);
	BookingEntity delete(Integer id);
	BookingEntity findById(Integer id);
	BookingEntity findByRefernceId(String refernceId);
}
