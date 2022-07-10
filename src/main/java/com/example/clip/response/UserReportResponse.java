package com.example.clip.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserReportResponse {
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("payments_sum")
    private BigDecimal paymentsSum;
    @JsonProperty("new_payments")
    private int newPayments;
    @JsonProperty("new_payments_amount")
    private BigDecimal newPaymentsAmount;
}
