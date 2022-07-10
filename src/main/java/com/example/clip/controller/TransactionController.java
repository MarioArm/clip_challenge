package com.example.clip.controller;


import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentResponse;
import com.example.clip.response.ProcessDisbursementResponse;
import com.example.clip.service.DisbursementService;
import com.example.clip.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/clip")
public class TransactionController {


    private final DisbursementService disbursementService;
    private final PaymentService paymentService;

    public TransactionController(DisbursementService disbursementService, PaymentService paymentService) {
        this.disbursementService = disbursementService;
        this.paymentService = paymentService;
    }

    @Transactional
    @PostMapping(value = "/createPayload")
    public ResponseEntity<PaymentResponse> create(@RequestBody @Valid PaymentRequest paymentRequest) {
        log.debug("PaymentRequest received: " + paymentRequest);

        PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest);

        log.info(String.format("Payment Created Successfully for user: [%s]", paymentRequest.getUserId()));
        return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
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
