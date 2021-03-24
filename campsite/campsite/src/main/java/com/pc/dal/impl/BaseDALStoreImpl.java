package com.pc.dal.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.pc.common.EntityLogicException;
import com.pc.model.bo.BaseEntity;

/**
 * Simulates a table in a database.
 * 
 * NOTE: in the end adding H2 and Flyway probably would have been better
 * and take just as much time to implement... but I now appreciate more the
 * transaction space databases offer!
 */
abstract public class BaseDALStoreImpl<E extends BaseEntity> {

	//simulate database sequence
	private AtomicInteger pkGenerator = new AtomicInteger();
	
	//replace with a real database with DAO and JPA,
	//use a Map to simulate atomic access
	//package protected to facilitate subclassers
	ConcurrentHashMap<Integer, E> dataBase = new ConcurrentHashMap<>();
	
	/**
	 * Perform a save or update as needed
	 */
	//@Transactional simulated with synchronized
	synchronized public E saveUpdate(E entity) {

		if(entity == null) {
			throw new EntityLogicException("no entity to save", "entity was null");
		}
		
		if(entity.getId() == null) {
			ensureBeforeNewAddConstraints(entity);
			entity.setId(pkGenerator.incrementAndGet());
		} else {
			ensureVersion(entity);
		}
		
		entity.setUpdated(LocalDateTime.now());
		entity.setVersion(entity.getVersion() + 1);
		dataBase.put(entity.getId(), entity);
		
		return entity;
	}
	
	synchronized public List<E> saveUpdate(List<E> entities) {
		
		ArrayList<E> success = new ArrayList<>();
		try {
			entities.stream().forEach(bo -> {
				//if the save fails then a partial success list will exist
				//to be used for the rollback
				E e = saveUpdate(bo);
				success.add(e);
			});			
		} catch (Exception e) {
			//silent catch
		}
		
		if(success.size() != entities.size()) {
			//we had an error so rollback
			success.stream().forEach(bo -> {
				delete(bo.getId());
			});
			
			throw new EntityLogicException("error saving list", "entity error saving from a list, rollbacked");
		}
		
		return entities;
	}
	
	//simulate optimistic locking
	synchronized protected void ensureVersion(E newEntity) {
		E oldEntity = dataBase.get(newEntity.getId());
		if(!oldEntity.getVersion().equals(newEntity.getVersion())) {
			throw new RuntimeException("incorrect version");
		}
	}
	
	//simulate constraints, there could have been a chance that since
	//the last check an entity was added.  since we're synchronized from the 
	//entry point to here it's a final chance to esnure we're consistent
	synchronized protected void ensureBeforeNewAddConstraints(E newEntity) {
	}
	
	//@Transactional simulated with synchronized
	synchronized public E delete(Integer id) {
		return dataBase.remove(id);
	}
	
	//@Transactional simulated with synchronized
	synchronized public List<E> delete(List<E> entities) {
		ArrayList<E> success = new ArrayList<>();
		try {
			entities.stream().forEach(bo -> {
				//if the save fails then a partial success list will exist
				//to be used for the rollback
				E e = delete(bo.getId());
				success.add(e);
			});			
		} catch (Exception e) {
			//silent catch
		}
		
		if(success.size() != entities.size()) {
			//we had an error so rollback
			success.stream().forEach(bo -> {
				saveUpdate(bo);
			});
			
			throw new EntityLogicException("error saving list", "entity error saving from a list, rollbacked");
		}
		
		return entities;		
	}
	
	public E findById(Integer id) {
		return dataBase.get(id);
	}
	
	public List<E> findAll() {
		ArrayList<E> all = new ArrayList<>();
		all.addAll(dataBase.values());
		return all;
	}
	
}

