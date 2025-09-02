package com.uber.ClientSocketService.controllers;

import com.uber.ClientSocketService.dtos.RideRequestDTO;
import com.uber.ClientSocketService.dtos.RideResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/socket")
@Log4j2
public class DriverRequestController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public DriverRequestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/new_ride")
    public ResponseEntity<Boolean> raiseRideRequest(@RequestBody RideRequestDTO requestDTO) {
        sendDriverRideRequest(requestDTO);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @MessageMapping("/ride_response/{passengerId}")
    public void rideResponse(@DestinationVariable Long passengerId, @Payload RideResponseDTO responseDTO) {
        System.out.println("Ride response: " +  responseDTO);
    }

    public void sendDriverRideRequest(RideRequestDTO rideRequestDTO) {
        log.info("Sending Driver Ride Request");
        simpMessagingTemplate.convertAndSend("/topic/rideRequest", rideRequestDTO);
    }
}
