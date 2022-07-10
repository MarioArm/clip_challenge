package com.example.clip.service;

import com.example.clip.model.Payment;
import com.example.clip.model.PaymentStatus;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.response.DisbursementDetails;
import com.example.clip.response.ProcessDisbursementResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DisbursementService {

    private final PaymentRepository paymentRepository;

    public DisbursementService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public ProcessDisbursementResponse processDisbursement() {
        Map<String, DisbursementDetails> map = new HashMap<>();
        List<Payment> newPaymentList =
                paymentRepository.findAllByStatus(PaymentStatus.NEW);

        newPaymentList.forEach(payment -> {
            map.compute(
                    payment.getUserId(),
                    (userId, disbursementDetails) -> Objects.isNull(disbursementDetails) ?
                            DisbursementDetails.builder()
                                    .payment(payment.getAmount().setScale(2, RoundingMode.DOWN))
                                    .build() :
                            DisbursementDetails.builder()
                                    .payment(disbursementDetails.getPayment().add(payment.getAmount()))
                                    .build());
            paymentRepository.save(payment.setStatus(PaymentStatus.PROCESSED));
        });

        map.forEach((userId, disbursementDetails) -> map.put(
                userId,
                disbursementDetails.setDisbursement(
                        disbursementDetails.getPayment()
                                .multiply(BigDecimal.valueOf(.965))
                                .setScale(2, RoundingMode.DOWN))));

        return ProcessDisbursementResponse.builder()
                .disbursementDetailsMap(map)
                .build();
    }
}
