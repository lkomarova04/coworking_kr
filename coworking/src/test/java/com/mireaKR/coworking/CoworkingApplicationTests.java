package com.mireaKR.coworking;

import com.mireaKR.coworking.controller.AuthController;
import com.mireaKR.coworking.controller.BookingController;
import com.mireaKR.coworking.controller.RoomController;
import com.mireaKR.coworking.controller.UserController;
import com.mireaKR.coworking.dto.LoginRequest;
import com.mireaKR.coworking.dto.Response;
import com.mireaKR.coworking.entity.Booking;
import com.mireaKR.coworking.entity.Room;
import com.mireaKR.coworking.entity.User;
import com.mireaKR.coworking.service.interfac.IBookingService;
import com.mireaKR.coworking.service.interfac.IRoomService;
import com.mireaKR.coworking.service.interfac.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CoworkingApplicationTests {

	@Mock
	private IUserService userService;
	@Mock
	private IBookingService bookingService;
	@Mock
	private IRoomService roomService;

	@InjectMocks
	private AuthController authController;
	@InjectMocks
	private BookingController bookingController;
	@InjectMocks
	private RoomController roomController;
	@InjectMocks
	private UserController userController;

	private User mockUser;
	private LoginRequest mockLoginRequest;
	private Room mockRoom;
	private Booking mockBooking;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		// Setup mock data
		mockUser = new User();
		mockUser.setEmail("test@test.com");
		mockUser.setPassword("password123");
		mockUser.setName("Test User");
		mockUser.setPhoneNumber("123456789");
		mockUser.setRole("USER");

		mockLoginRequest = new LoginRequest();
		mockLoginRequest.setEmail("test@test.com");
		mockLoginRequest.setPassword("password123");

		mockRoom = new Room();
		mockRoom.setId(1L);
		mockRoom.setRoomType("Office");
		mockRoom.setRoomPrice(BigDecimal.valueOf(100));
		mockRoom.setRoomDescription("A nice office room");

		mockBooking = new Booking();
		mockBooking.setId(1L);
		mockBooking.setCheckInDate(LocalDate.now());
		mockBooking.setCheckOutDate(LocalDate.now().plusDays(1));
		mockBooking.setBookingConfirmationCode("CONF123");
		mockBooking.setRoom(mockRoom);
		mockBooking.setUser(mockUser);
	}

	// Test AuthController
	@Test
	public void testRegister() {
		Response mockResponse = new Response();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("User registered successfully");

		when(userService.register(any(User.class))).thenReturn(mockResponse);

		ResponseEntity<Response> response = authController.register(mockUser);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("User registered successfully", response.getBody().getMessage());
	}

	@Test
	public void testLogin() {
		Response mockResponse = new Response();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("Login successful");

		when(userService.login(any(LoginRequest.class))).thenReturn(mockResponse);

		ResponseEntity<Response> response = authController.login(mockLoginRequest);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Login successful", response.getBody().getMessage());
	}

	// Test BookingController
	@Test
	public void testSaveBooking() {
		Response mockResponse = new Response();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("Booking saved successfully");

		when(bookingService.saveBooking(anyLong(), anyLong(), any(Booking.class))).thenReturn(mockResponse);

		ResponseEntity<Response> response = bookingController.saveBookings(1L, 1L, mockBooking);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Booking saved successfully", response.getBody().getMessage());
	}

	@Test
	public void testCancelBooking() {
		Response mockResponse = new Response();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("Booking canceled successfully");

		when(bookingService.cancelBooking(anyLong())).thenReturn(mockResponse);

		ResponseEntity<Response> response = bookingController.cancelBooking(1L);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Booking canceled successfully", response.getBody().getMessage());
	}



	@Test
	public void testGetAvailableRooms() {
		Response mockResponse = new Response();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("Available rooms fetched successfully");

		when(roomService.getAllAvailableRooms()).thenReturn(mockResponse);

		ResponseEntity<Response> response = roomController.getAvailableRooms();
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Available rooms fetched successfully", response.getBody().getMessage());
	}

	// Test UserController
	@Test
	public void testGetLoggedInUserProfile() {
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("test@test.com");
		SecurityContextHolder.getContext().setAuthentication(authentication);

		Response mockResponse = new Response();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("User profile fetched successfully");

		when(userService.getMyInfo(anyString())).thenReturn(mockResponse);

		ResponseEntity<Response> response = userController.getLoggedInUserProfile();
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("User profile fetched successfully", response.getBody().getMessage());
	}

	@Test
	public void testDeleteUser() {
		Response mockResponse = new Response();
		mockResponse.setStatusCode(200);
		mockResponse.setMessage("User deleted successfully");

		when(userService.deleteUser(anyString())).thenReturn(mockResponse);

		ResponseEntity<Response> response = userController.deleteUSer("test@test.com");
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("User deleted successfully", response.getBody().getMessage());
	}
}
