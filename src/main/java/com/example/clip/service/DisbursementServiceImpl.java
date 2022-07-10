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
public class DisbursementServiceImpl implements DisbursementService {

    private final PaymentRepository paymentRepository;

    public DisbursementServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public ProcessDisbursementResponse processDisbursement() {
        List<Payment> newPaymentList = paymentRepository.findAllByStatus(PaymentStatus.NEW);

        Map<String, DisbursementDetails> disbursementDetailsMap = sumAllNewPaymentsIntoAMap(newPaymentList);
        computeDisbursementAmount(disbursementDetailsMap);

        return ProcessDisbursementResponse.builder()
                .disbursementDetailsMap(disbursementDetailsMap)
                .build();
    }

    private void computeDisbursementAmount(Map<String, DisbursementDetails> disbursementDetailsMap) {
        disbursementDetailsMap.forEach((userId, disbursementDetails) ->
                disbursementDetailsMap.put(
                        userId,
                        disbursementDetails.setDisbursement(
                                disbursementDetails.getPayment()
                                        .multiply(BigDecimal.valueOf(.965))
                                        .setScale(2, RoundingMode.DOWN))));
    }

    private Map<String, DisbursementDetails> sumAllNewPaymentsIntoAMap(List<Payment> newPaymentList) {
        Map<String, DisbursementDetails> map = new HashMap<>();
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
        return map;
    }
}
