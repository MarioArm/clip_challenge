package com.example.clip.controller;


import com.example.clip.model.Payment;
import com.example.clip.model.PaymentStatus;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentResponse;
import com.example.clip.response.ProcessDisbursementResponse;
import com.example.clip.service.DisbursementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.RoundingMode;


@Slf4j
@RestController
@RequestMapping("/api/clip")
public class TransactionController {

    private final PaymentRepository paymentRepository;
    private final DisbursementService disbursementService;

    public TransactionController(PaymentRepository paymentRepository, DisbursementService disbursementService) {
        this.paymentRepository = paymentRepository;
        this.disbursementService = disbursementService;
    }

    @Transactional
    @PostMapping(value = "/createPayload")
    public ResponseEntity<PaymentResponse> create(@RequestBody @Valid PaymentRequest paymentRequest) {
        log.debug("PaymentRequest received: " + paymentRequest);

        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount().setScale(2, RoundingMode.DOWN));
        payment.setUserId(paymentRequest.getUserId());
        payment.setStatus(PaymentStatus.NEW);

        Payment storedPayment = paymentRepository.save(payment);
        log.info("Payload Created Successfully");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PaymentResponse.builder().paymentId(storedPayment.getId()).build());
    }

    @Transactional
    @PostMapping(value = "/disbursement/process")
    public ResponseEntity<ProcessDisbursementResponse> processDisbursement() {
        log.debug("Request to process disbursement received:");
        ProcessDisbursementResponse response = disbursementService.processDisbursement();
        log.info("Disbursement processing was successful");
        return ResponseEntity.ok().body(response);
    }
}
