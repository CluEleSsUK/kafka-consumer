package wevat.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class PaymentRequest {
    private String currency;
    private String amount;
    private PaymentMethod method;
    private Object account;
}
