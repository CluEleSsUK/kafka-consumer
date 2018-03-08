package wevat;

import org.apache.kafka.clients.producer.ProducerRecord;
import wevat.data.PaymentQueueInteractor;
import wevat.entity.PaymentMethod;
import wevat.entity.PaymentRequest;
import wevat.service.PaymentService;

public class Main {
    public static void main(String[] args) {

        KafkaFactory.createPaymentRequestProducer().send(
                aPayment(PaymentRequest.builder()
                        .method(PaymentMethod.WECHAT_PAY)
                        .currency("EUR")
                        .amount("111.00")
                        .build()));


        PaymentQueueInteractor interactor = new PaymentQueueInteractor(new PaymentService());

        interactor.onStart();

    }

    private static ProducerRecord<Long, PaymentRequest> aPayment(PaymentRequest request) {
        return new ProducerRecord<>(KafkaFactory.PAYMENT_REQUEST_TOPIC, System.currentTimeMillis(), request);
    }

}
