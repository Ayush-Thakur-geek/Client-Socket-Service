package com.uber.ClientSocketService.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExactLocation {

    private Double longitude;
    private Double latitude;

}