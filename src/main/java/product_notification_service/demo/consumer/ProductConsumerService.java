package product_notification_service.demo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.kafka.support.KafkaHeaders;

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

//    // --- ADD THE CONSUMER LISTENER FOR THE PRICE UPDATES TOPIC HERE ---
//    @KafkaListener(
//            topics = "product-price-updates",
//            groupId = "product_processing_group",
//            containerFactory = "kafkaListenerContainerFactory"
//    )
//    public void consumePriceUpdate(
//            @Payload String message,
//            @Header(KafkaHeaders.RECEIVED_KEY) String productId,
//            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
//
//        System.out.println("\n==================================================");
//        System.out.println("RECEIVED KAFKA EVENT IN NOTIFICATION SERVICE");
//        System.out.println("Topic: product-price-updates");
//        System.out.println("Product ID (Key): " + productId);
//        System.out.println("Partition: " + partition);
//        System.out.println("Payload Data: " + message);
//        System.out.println("==================================================\n");
//
//        // Your custom logic to trigger alerts/emails goes here
//    }
}
