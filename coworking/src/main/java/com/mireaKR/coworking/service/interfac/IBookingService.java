package com.mireaKR.coworking.service.interfac;

import com.mireaKR.coworking.dto.Response;
import com.mireaKR.coworking.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}