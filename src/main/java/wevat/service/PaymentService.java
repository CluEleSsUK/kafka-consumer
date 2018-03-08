package wevat.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import wevat.entity.PaymentRequest;

public class PaymentService {

    public void process(ConsumerRecord<Long, PaymentRequest> paymentRequest) {
        System.out.println("the payment request is " + paymentRequest);
    }
}
