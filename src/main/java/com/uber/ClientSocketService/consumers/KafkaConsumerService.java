package com.uber.ClientSocketService.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(groupId = "sample-group", topics="driverResponse-topic")
    public void listen(String message) {
        System.out.println("Kafka message from sample topic: " + message);
    }

}
