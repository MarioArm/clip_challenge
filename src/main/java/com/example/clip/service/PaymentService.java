package com.example.clip.service;

import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest paymentRequest);
}
