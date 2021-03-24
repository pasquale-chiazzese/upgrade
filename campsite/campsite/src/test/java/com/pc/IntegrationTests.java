package com.pc;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pc.common.Utils;
import com.pc.model.dto.BookingDTO;
import com.pc.model.dto.BookingRequestDTO;
import com.pc.model.dto.CampSiteAvailableDTO;
import com.pc.model.dto.CampSiteSpaceDTO;

/**
 * Using Junit4 and its easy setup and running to perform the integration tests.
 * 
 * See AppConfig for some default data.
 * 
 * notes: https://sormuras.github.io/blog/2018-09-13-junit-4-core-vs-jupiter-api.html
 */
public class IntegrationTests {

	ThreadPoolExecutor executor;
	HttpClient client = HttpClient.newHttpClient();
	
	@Before
	public void setup() {
		try {
            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
			executor = new ThreadPoolExecutor(20, 20, 100, TimeUnit.SECONDS, workQueue);
			CampSiteApplication.main(new String[0]);
			//wait for the app to start
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void teardown() {
		try {
			executor.shutdown();
			
			HttpRequest request = HttpRequest.newBuilder()
			      .uri(URI.create("http://localhost:8080/actuator/shutdown"))
			      .POST(BodyPublishers.noBody())
			      .build();
			
			client.sendAsync(request, BodyHandlers.ofString())
			      .thenApply(HttpResponse::body)
			      .thenAccept(System.out::println)
			      .join();	
			//wait for the app to shutdown
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String sendGet(String url) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
			      .uri(URI.create(url))
			      .GET()
			      .build();
			
			return sendRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String sendDelete(String url) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
			      .uri(URI.create(url))
			      .DELETE()
			      .build();
			
			return sendRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String sendPut(String url, String jsonPayload) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
			      .uri(URI.create(url))
			      .PUT(BodyPublishers.ofString(jsonPayload))
			      .header("Accept", "application/json")
			      .header("Content-type", "application/json")
			      .build();
			
			return sendRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String sendPost(String url, String jsonPayload) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
			      .uri(URI.create(url))
			      .POST(BodyPublishers.ofString(jsonPayload))
			      .header("Accept", "application/json")
			      .header("Content-type", "application/json")
			      .build();
			
			return sendRequest(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String sendRequest(HttpRequest request) throws Exception{
		
	    HttpResponse<?> response = client.send(request, BodyHandlers.ofString());
	    String body = (String) response.body();
		System.out.println(body);
		
		if(response.statusCode() == 200) {
			return body;
		}
		
		return null;
	}
	
	//************************************
	//Simple tests of all the APIs
	//************************************
	@Test
	public void test_CampSiteSpaceController_findAllCampSiteSpace() {
		System.out.println("test_CampSiteSpaceController_findAllCampSiteSpace************************************************************");
		sendGet("http://localhost:8080/api/v1/campsitespaces");
		System.out.println("************************************************************");
	}
	
	@Test
	public void test_CampSiteSpaceController_findCampSiteSpacesByRrefernceId() {
		System.out.println("test_CampSiteSpaceController_findCampSiteSpacesByRrefernceId************************************************************");
		sendGet("http://localhost:8080/api/v1/campsitespaces/test_ref_1");
		System.out.println("************************************************************");
	}
	
	@Test
	public void test_CampSiteController_findAvaiableSpaces() {
		System.out.println("test_CampSiteController_findAvaiableSpaces************************************************************");
		sendGet("http://localhost:8080/api/v1/campsite");
		System.out.println("************************************************************");
	}
	
	@Test
	public void test_CampSiteController_findAvaiableSpaces_dates() {
		System.out.println("test_CampSiteController_findAvaiableSpaces_dates************************************************************");
		sendGet("http://localhost:8080/api/v1/campsite/2021-03-20/2021-03-30");
		System.out.println("************************************************************");
	}
	
	@Test
	public void test_CampSiteController_findBooking() {
		System.out.println("test_CampSiteController_findBooking************************************************************");
		sendGet("http://localhost:8080/api/v1/booking/test_ref_1");
		System.out.println("************************************************************");
	}

	@Test
	public void test_CampSiteController_cancelBooking() {
		System.out.println("test_CampSiteController_cancelBooking************************************************************");
		sendDelete("http://localhost:8080/api/v1/booking/test_ref_1");
		System.out.println("************************************************************");
	}

	@Test
	public void test_CampSiteController_bookAvaiableSpaces() {
		System.out.println("test_CampSiteController_bookAvaiableSpaces************************************************************");
		try {
			BookingRequestDTO dto = new BookingRequestDTO();
			dto.setName("P C");
			dto.setEmail("email@email.com");
			dto.setStartInclusive(LocalDate.parse("2021-04-01"));
			dto.setEndInclusive(LocalDate.parse("2021-04-03"));
			
			String dtoJson = Utils.getMapper().writeValueAsString(dto);
			String postJson = sendPost("http://localhost:8080/api/v1/bookingrequest", dtoJson);
			
			if(postJson != null) {
				BookingDTO returnDto = Utils.getMapper().readValue(postJson, BookingDTO.class);
				System.out.println(returnDto.getRefernceId());
			} else {
				System.out.println("error no RefernceId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("************************************************************");
	}
	
	//************************************
	//Advanced tests with reservations
	//************************************

	@Test
	public void test_CampSiteController_updateBooking_simple() throws Exception {
		System.out.println("test_CampSiteController_updateBooking_simple************************************************************");
    	ArrayList<CampSiteSpaceDTO> spaces = new ArrayList<>();
    	CampSiteSpaceDTO spaceDto = new CampSiteSpaceDTO();
    	spaceDto.setRefernceId("test_ref_2");
    	spaceDto.setReservedDate(LocalDate.now().plus(10, ChronoUnit.DAYS));
    	spaceDto.setHolderEmail("update@test.com");
    	spaces.add(spaceDto);
    	
    	spaceDto = new CampSiteSpaceDTO();
    	spaceDto.setRefernceId("test_ref_2");
    	spaceDto.setReservedDate(LocalDate.now().plus(11, ChronoUnit.DAYS));
    	spaceDto.setHolderEmail("update@test.com");
    	spaces.add(spaceDto);
    	
    	BookingDTO bookDto = new BookingDTO();
    	bookDto.setRefernceId("test_ref_2");
    	bookDto.setEmail("update@test.com");
    	bookDto.setFullName("Update Booking");
    	bookDto.setDatesBooked(spaces);
    	
		try {
			String dtoJson = Utils.getMapper().writeValueAsString(bookDto);
			String postJson = sendPut("http://localhost:8080/api/v1/booking/" + bookDto.getRefernceId(), 
										dtoJson);
			
			if(postJson != null) {
				BookingDTO returnDto = Utils.getMapper().readValue(postJson, BookingDTO.class);
				System.out.println(returnDto.getRefernceId());
			} else {
				System.out.println("error no RefernceId");
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}    	
		System.out.println("************************************************************");
	}
	
	@Test
	public void test_CampSiteController_sequential_add_bookingRequest_and_update_it() {
		System.out.println("test_CampSiteController_sequential_add_bookingRequest_and_update************************************************************");

		BookingRequestDTO br = new BookingRequestDTO();
		br.setName("P C");
		br.setEmail("test@test.com");
		br.setStartInclusive(LocalDate.now().plus(10, ChronoUnit.DAYS));
		br.setEndInclusive(LocalDate.now().plus(11, ChronoUnit.DAYS));
    	
		try {
			String dtoJson = Utils.getMapper().writeValueAsString(br);
			String postJson = sendPost("http://localhost:8080/api/v1/bookingrequest", dtoJson);
			
			BookingDTO returnDto = Utils.getMapper().readValue(postJson, BookingDTO.class);
			System.out.println(returnDto.getRefernceId());
			
			//remove a date for the update
			returnDto.getDatesBooked().remove(0);
			
			dtoJson = Utils.getMapper().writeValueAsString(returnDto);
			postJson = sendPut("http://localhost:8080/api/v1/booking/" + returnDto.getRefernceId(), 
										dtoJson);
			if(postJson != null) {
				returnDto = Utils.getMapper().readValue(postJson, BookingDTO.class);
				System.out.println(returnDto.getRefernceId());
			} else {
				System.out.println("error no RefernceId");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}    	
		sendGet("http://localhost:8080/api/v1/campsitespaces");
		System.out.println("************************************************************");
	}
	
	@Test
	public void test_CampSiteController_threaded_add_bookingRequest() {
		System.out.println("test_CampSiteController_threaded_add_bookingRequest************************************************************");
		
		//one of these add booking will fail as the dates overlap.  in theroy most runs
		//it will be the first submitted wins, but it really depends on the executor
		//and how it handles FIFO (if any)
		executor.submit(() -> {
			try {
				BookingRequestDTO dto = new BookingRequestDTO();
				dto.setName("P C");
				dto.setEmail("email1@email1.com");
				dto.setStartInclusive(LocalDate.now().plus(16, ChronoUnit.DAYS));
				dto.setEndInclusive(LocalDate.now().plus(18, ChronoUnit.DAYS));
				
				String dtoJson = Utils.getMapper().writeValueAsString(dto);
				String postJson = sendPost("http://localhost:8080/api/v1/bookingrequest", dtoJson);

				if(postJson != null) {
					BookingDTO returnDto = Utils.getMapper().readValue(postJson, BookingDTO.class);
					System.out.println(returnDto.getRefernceId());
				} else {
					System.out.println("error no RefernceId");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}			
		});
		
		executor.submit(() -> {
			try {
				BookingRequestDTO dto = new BookingRequestDTO();
				dto.setName("P C");
				dto.setEmail("email2@email2.com");
				dto.setStartInclusive(LocalDate.now().plus(18, ChronoUnit.DAYS));
				dto.setEndInclusive(LocalDate.now().plus(20, ChronoUnit.DAYS));
				
				String dtoJson = Utils.getMapper().writeValueAsString(dto);
				String postJson = sendPost("http://localhost:8080/api/v1/bookingrequest", dtoJson);
				
				if(postJson != null) {
					BookingDTO returnDto = Utils.getMapper().readValue(postJson, BookingDTO.class);
					System.out.println(returnDto.getRefernceId());
				} else {
					System.out.println("error no RefernceId");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}			
		});
		
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//output all dates to ensure consistent
		System.out.println("");
		System.out.println("");
		System.out.println("");
		sendGet("http://localhost:8080/api/v1/campsitespaces");
		System.out.println("************************************************************");
	}
	
	@Test
	public void test_CampSiteController_threaded_book_whole_month() {
		System.out.println("test_CampSiteController_threaded_book_whole_month************************************************************");
		
		//threads will randomly try to book the whole month
		for (int i = 0; i < 15; i++) {
			executor.submit(() -> {
				try {
					//set a retry limit
					for (int x = 0; x < 10; x++) {
						try {
							boolean autoBook = autoBook();
							if(autoBook) {
								break;
							}		
							//let the other threads try to book
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});			
		}
		
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//output all dates to ensure consistent
		System.out.println("");
		System.out.println("");
		System.out.println("");
		sendGet("http://localhost:8080/api/v1/campsitespaces");
		System.out.println("************************************************************");
	}
	
	private boolean autoBook() {
		String campsitespacesJson = sendGet("http://localhost:8080/api/v1/campsite");
		List<CampSiteAvailableDTO> campsitespaces = null;
		try {
			campsitespaces = Utils.getMapper().readValue(
											campsitespacesJson, 
											new TypeReference<List<CampSiteAvailableDTO>>() {});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(CollectionUtils.isEmpty(campsitespaces)) {
			//break
			return true;
		}
		Random r = new Random();
	    int randomIndex = -1;
	    int stayDuration = -1;
		
	    //as we have less dates avaiable use small windows
	    if(campsitespaces.size() > 7) {
		    randomIndex = r.nextInt(campsitespaces.size());
		    stayDuration = r.nextInt(3);
	    } else if(campsitespaces.size() > 4) {
		    randomIndex = r.nextInt(campsitespaces.size());
		    stayDuration = r.nextInt(2);
	    } else {
		    randomIndex = r.nextInt(campsitespaces.size());
		    stayDuration = 0;
	    }
	    
	    LocalDate startDay = campsitespaces.get(randomIndex).getDate();
		
	    String name = Thread.currentThread().getName().replaceAll("-", "");
		BookingRequestDTO dto = new BookingRequestDTO();
		dto.setName("Mr " + name);
		dto.setEmail(name + "@email.com");
		dto.setStartInclusive(startDay);
		dto.setEndInclusive(startDay.plus(stayDuration, ChronoUnit.DAYS));
		
		try {		
			String dtoJson = Utils.getMapper().writeValueAsString(dto);
			String postJson = sendPost("http://localhost:8080/api/v1/bookingrequest", dtoJson);
			
			if(StringUtils.isBlank(postJson)) {
				return false;
			}
			BookingDTO returnDto = Utils.getMapper().readValue(postJson, BookingDTO.class);
			if(StringUtils.isNotBlank(returnDto.getRefernceId())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	
}


