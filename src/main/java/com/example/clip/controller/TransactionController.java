package com.example.clip.controller;


import com.example.clip.model.Payment;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/clip")
public class TransactionController {

    PaymentRepository paymentRepository;

    public TransactionController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @PostMapping(value = "/createPayload")
    public ResponseEntity<PaymentResponse> create(@RequestBody @Valid PaymentRequest paymentRequest) {
        log.debug("PaymentRequest received: " + paymentRequest);

        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setUserId(paymentRequest.getUserId());

        Payment storedPayment = paymentRepository.save(payment);
        log.info("Payload Created Successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PaymentResponse.builder().paymentId(storedPayment.getId()).build());
    }
}
