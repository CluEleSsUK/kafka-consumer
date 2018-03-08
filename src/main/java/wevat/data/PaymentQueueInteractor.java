package wevat.data;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import wevat.KafkaFactory;
import wevat.entity.PaymentRequest;
import wevat.service.PaymentService;

public class PaymentQueueInteractor {

    private volatile boolean isRunning = false;
    private final PaymentService paymentService;

    private Consumer<Long, PaymentRequest> paymentRequestConsumer;

    public PaymentQueueInteractor(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void onStart() {
        isRunning = true;
        initConsumerIfNeeded();

        spawnThread(() -> {
            while (isRunning) {
                pollConsumer();
            }
        });
    }

    public void onStop() {
        isRunning = false;
    }

    private void initConsumerIfNeeded() {
        if (paymentRequestConsumer == null) {
            paymentRequestConsumer = KafkaFactory.createPaymentRequestConsumer();
        }
    }

    private void spawnThread(Runnable r) {
        new Thread(r).start();
    }

    private void pollConsumer() {
        ConsumerRecords<Long, PaymentRequest> records = paymentRequestConsumer.poll(0);
        records.forEach(paymentService::process);
    }

}
