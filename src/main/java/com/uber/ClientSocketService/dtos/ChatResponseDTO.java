package com.uber.ClientSocketService.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponseDTO {

    private String name;
    private String message;
    private String timeStamp;

}
