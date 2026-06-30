package product_notification_service.demo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component // Best Practice: Decouples business listener logic cleanly from system configuration infrastructure
public class PriceUpdateConsumer {

    @KafkaListener(
            topics = "product-price-updates",
            groupId = "product_price_notification_group_v3", // Use unique group IDs per functional requirement task
            containerFactory = "kafkaListenerContainerFactory" // <-- CRITICAL: Connects it to your KafkaConfig bean!
    )
    public void consumePriceUpdate(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_KEY) String productId,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {

        System.out.println("\n==================================================");
        System.out.println("🔥 SUCCESS: RECEIVED KAFKA EVENT ON LOCALHOST 🔥");
        System.out.println("Topic: product-price-updates");
        System.out.println("Product ID Key: " + productId);
        System.out.println("Partition Location: " + partition);
        System.out.println("Incoming Payload: " + message);
        System.out.println("==================================================\n");

        // Add downstream business workflows here (e.g., SMS alerts, Webhook triggers)
    }
}