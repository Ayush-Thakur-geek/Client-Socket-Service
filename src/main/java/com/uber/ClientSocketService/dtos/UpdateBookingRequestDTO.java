package com.uber.ClientSocketService.dtos;

import com.uber.UberEntityService.models.BookingStatus;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingRequestDTO {

    private Optional<Long> driverId;
    private BookingStatus bookingStatus;

}
