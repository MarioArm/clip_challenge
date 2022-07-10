package com.example.clip.service;

import com.example.clip.model.Payment;
import com.example.clip.model.PaymentStatus;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentResponse;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount().setScale(2, RoundingMode.DOWN));
        payment.setUserId(paymentRequest.getUserId());
        payment.setStatus(PaymentStatus.NEW);

        Payment storedPayment = paymentRepository.save(payment);

        return PaymentResponse.builder().paymentId(storedPayment.getId()).build();
    }
}
