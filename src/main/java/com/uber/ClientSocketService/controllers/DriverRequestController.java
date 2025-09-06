package com.uber.ClientSocketService.controllers;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import com.uber.ClientSocketService.apis.BookingServiceApi;
import com.uber.ClientSocketService.dtos.RideRequestDTO;
import com.uber.ClientSocketService.dtos.RideResponseDTO;
import com.uber.ClientSocketService.dtos.UpdateBookingRequestDTO;
import com.uber.ClientSocketService.dtos.UpdateBookingResponseDTO;
import com.uber.UberEntityService.models.BookingStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/socket")
@Log4j2
public class DriverRequestController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    RestTemplate restTemplate;
    private final BookingServiceApi bookingServiceApi;
    EurekaClient eurekaClient;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate, BookingServiceApi bookingServiceApi, EurekaClient eurekaClient) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.bookingServiceApi = bookingServiceApi;
        this.restTemplate = new RestTemplate();
        this.eurekaClient = eurekaClient;
        this.eurekaClient = eurekaClient;
    }

    @PostMapping("/new_ride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDTO requestDTO) {
        sendDriverRideRequest(requestDTO);
        getApplicationsRegistered();
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    private void getApplicationsRegistered() {
        eurekaClient.getApplicationInfoManager().refreshDataCenterInfoIfRequired();
        Applications applications = eurekaClient.getApplications();
        applications.getRegisteredApplications().forEach(app -> {
            System.out.println("Application name: " + app.getName());
        });
    }

    private String getServiceUrl(String serviceName) {
        String baseUrl = eurekaClient.getNextServerFromEureka(serviceName, false).getHomePageUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        return baseUrl;
    }

    @MessageMapping("/ride_response/{passengerId}")
    public void rideResponse(@DestinationVariable Long passengerId, @Payload RideResponseDTO responseDTO) {
        System.out.println("Ride response booking id: " +  responseDTO.getBookingId() + " passengerId: " + passengerId);
        UpdateBookingRequestDTO updateBookingRequestDTO = UpdateBookingRequestDTO.builder()
                .driverId(Optional.of(responseDTO.getDriverId()))
                .bookingStatus(BookingStatus.SCHEDULED)
                .build();
//        UpdateBookingResponseDTO updateBookingResponseDTO = (UpdateBookingResponseDTO) bookingServiceApi.updateBooking(updateBookingRequestDTO, passengerId);
//        System.out.println(updateBookingResponseDTO);
        System.out.println("bookingId: " + responseDTO.getBookingId());
        System.out.println(getServiceUrl("UBERBOOKINGSERVICE"));
        String url = String.format("%s/api/v1/booking/%d", getServiceUrl("UBERBOOKINGSERVICE"), responseDTO.getBookingId());
        this.restTemplate.put(url, updateBookingRequestDTO);

//        this.restTemplate.put(getServiceUrl("UBERBOOKINGSERVICE") + responseDTO.getBookingId(), updateBookingRequestDTO);
    }

    public void sendDriverRideRequest(RideRequestDTO rideRequestDTO) {
        log.info("Sending Driver Ride Request");
        simpMessagingTemplate.convertAndSend("/topic/rideRequest", rideRequestDTO);
    }
}
