package com.GroceryOrderingSystem.listener;

import com.GroceryOrderingSystem.GroceryOrderingSystemApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = GroceryOrderingSystemApplication.RES_TOPIC, groupId = "group_response", containerFactory = "kafkaListenerFactory")
    public void consume(String response) throws Exception {
        System.out.println(response);
    }
}
