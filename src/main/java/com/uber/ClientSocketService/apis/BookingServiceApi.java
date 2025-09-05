package com.uber.ClientSocketService.apis;

import com.uber.ClientSocketService.dtos.UpdateBookingRequestDTO;
import com.uber.ClientSocketService.dtos.UpdateBookingResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BookingServiceApi {

    @POST("/api/v1/booking/{bookingId}")
    Call<UpdateBookingResponseDTO> updateBooking(@Body UpdateBookingRequestDTO updateBookingRequestDTO, @Path("bookingId") Long bookingId);
}
