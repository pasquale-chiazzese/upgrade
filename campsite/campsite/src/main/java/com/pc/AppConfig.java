package com.pc;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.pc.dal.BookingsStore;
import com.pc.dal.SpacesStore;
import com.pc.model.bo.BookingEntity;
import com.pc.model.bo.CampSiteSpaceEntity;

@Configuration
@ComponentScan(basePackages = "com.pc")
public class AppConfig {

	/**
	 * Fill with some test data
	 * 
	 */
    @Bean
    public CommandLineRunner run(BookingsStore bookingsStore, SpacesStore spacesStore) throws Exception {
        return args -> {
        	ArrayList<CampSiteSpaceEntity> spaces = new ArrayList<>();
        	CampSiteSpaceEntity bo = new CampSiteSpaceEntity();
        	bo.setRefernceId("test_ref_1");
        	bo.setReservedDate(LocalDate.now().plus(5, ChronoUnit.DAYS));
        	bo.setHolderEmail("test@test.com");
        	spacesStore.saveUpdate(bo);
        	spaces.add(bo);
        	
        	bo = new CampSiteSpaceEntity();
        	bo.setRefernceId("test_ref_1");
        	bo.setReservedDate(LocalDate.now().plus(6, ChronoUnit.DAYS));
        	bo.setHolderEmail("test@test.com");
        	spacesStore.saveUpdate(bo);
        	spaces.add(bo);
        	
        	BookingEntity be = new BookingEntity();
        	be.setRefernceId("test_ref_1");
        	be.setEmail("test@test.com");
        	be.setFullName("P1 C1");
        	be.setDatesBooked(spaces);
        	bookingsStore.saveUpdate(be);
        	
        	//another
        	spaces = new ArrayList<>();
        	
        	bo = new CampSiteSpaceEntity();
        	bo.setRefernceId("test_ref_2");
        	bo.setReservedDate(LocalDate.now().plus(4, ChronoUnit.DAYS));
        	bo.setHolderEmail("test2@test2.com");
        	spacesStore.saveUpdate(bo);
        	spaces.add(bo);
        	
        	be = new BookingEntity();
        	be.setRefernceId("test_ref_2");
        	be.setEmail("test2@test2.com");
        	be.setFullName("P2 C2");
        	be.setDatesBooked(spaces);
        	bookingsStore.saveUpdate(be);        	
        };
    }
    
    
}
