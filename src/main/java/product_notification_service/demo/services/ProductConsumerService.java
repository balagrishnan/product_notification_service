package product_notification_service.demo.services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumerService {
    // You MUST specify the exact topic name "product_updates" here!
    @KafkaListener(
            topics = "product_updates",
            groupId = "product_processing_group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeProductEvent(String jsonMessage){
        System.out.println("\n=================================================");
        System.out.println("🚨 WORKER MICROSERVICE RECEIVED EVENT: " + jsonMessage);
        System.out.println("=================================================");
    }
}
