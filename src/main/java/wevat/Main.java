package wevat;

import org.apache.kafka.clients.producer.ProducerRecord;
import wevat.data.PaymentQueueInteractor;
import wevat.entity.PaymentMethod;
import wevat.entity.PaymentRequest;
import wevat.service.PaymentService;

public class Main {
    public static void main(String[] args) {
        //create a payment
        ProducerRecord<Long, PaymentRequest> payment = aPayment(PaymentRequest.builder()
                .method(PaymentMethod.WECHAT_PAY)
                .currency("EUR")
                .amount("111.00")
                .build());

        //send it to the producer
        KafkaFactory.createPaymentRequestProducer().send(payment);

        //create the consumer and start it
        PaymentQueueInteractor interactor = new PaymentQueueInteractor(new PaymentService());

        interactor.onStart();

        //go view stdout, and behold the payment!
    }

    private static ProducerRecord<Long, PaymentRequest> aPayment(PaymentRequest request) {
        return new ProducerRecord<>(KafkaFactory.PAYMENT_REQUEST_TOPIC, System.currentTimeMillis(), request);
    }

}
