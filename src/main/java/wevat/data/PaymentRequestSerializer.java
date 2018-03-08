package wevat.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import wevat.entity.PaymentRequest;

import java.util.Map;

public class PaymentRequestSerializer implements Serializer<PaymentRequest> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //no-op
    }

    @Override
    public byte[] serialize(String topic, PaymentRequest data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        //no-op
    }
}
