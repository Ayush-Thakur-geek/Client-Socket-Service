package com.uber.ClientSocketService.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    // Custom constructor for JSON deserialization
    @JsonCreator
    public UpdateBookingRequestDTO(
            @JsonProperty("driverId") Long driverId,
            @JsonProperty("bookingStatus") BookingStatus bookingStatus) {
        this.driverId = Optional.ofNullable(driverId);
        this.bookingStatus = bookingStatus;
    }

    // Helper methods for easier JSON handling
    public Long getDriverIdValue() {
        return driverId.orElse(null);
    }

    public void setDriverIdValue(Long driverId) {
        this.driverId = Optional.ofNullable(driverId);
    }
}
