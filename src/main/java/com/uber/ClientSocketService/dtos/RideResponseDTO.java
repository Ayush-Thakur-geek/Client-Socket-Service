package com.uber.ClientSocketService.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponseDTO {
    long driverId;
    long bookingId;
    String status;
}
