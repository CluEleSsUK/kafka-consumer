package wevat.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import wevat.entity.PaymentRequest;

import java.util.Map;

public class PaymentRequestDeserializer implements Deserializer<PaymentRequest> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //do nowt
    }

    @Override
    public PaymentRequest deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            return mapper.readValue(data, PaymentRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        //do nowt
    }
}
