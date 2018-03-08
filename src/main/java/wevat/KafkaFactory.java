package wevat;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import wevat.data.PaymentRequestDeserializer;
import wevat.data.PaymentRequestSerializer;
import wevat.entity.PaymentRequest;

import java.io.Closeable;
import java.util.Collections;
import java.util.Properties;

public class KafkaFactory {

    public static final String PAYMENT_REQUEST_TOPIC = "PAYMENT_REQUEST_TOPIC";

    private static final String brokers = "localhost:9092";
    private static final String clientId = "PaymentsGateway";

    public static Producer<Long, String> createLoggingProducer() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "INFO");

        KafkaProducer<Long, String> producer = new KafkaProducer<>(props);
        addShutdownHookFor(producer);

        return producer;
    }

    public static Producer<Long, PaymentRequest> createPaymentRequestProducer() {
        KafkaProducer<Long, PaymentRequest> producer = new KafkaProducer<>(paymentRequestProducerProps());
        addShutdownHookFor(producer);

        return producer;
    }

    private static Properties paymentRequestProducerProps() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, PaymentRequestSerializer.class.getName());
        props.put(ProducerConfig.METRICS_RECORDING_LEVEL_CONFIG, "INFO");
        return props;
    }

    public static Consumer<Long, PaymentRequest> createPaymentRequestConsumer() {
        KafkaConsumer<Long, PaymentRequest> consumer = new KafkaConsumer<>(paymentRequestConsumerProps());
        consumer.subscribe(Collections.singletonList(PAYMENT_REQUEST_TOPIC));
        addShutdownHookFor(consumer);

        return consumer;
    }

    private static Properties paymentRequestConsumerProps() {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "some id");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PaymentRequestDeserializer.class.getName());

        return props;
    }

    private static void addShutdownHookFor(Closeable component) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                component.close();
            } catch (Exception ignore) {}
        }));
    }

}

